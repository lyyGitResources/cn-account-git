package com.hisupplier.cn.account.menu;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.misc.VideoDAO;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.UploadUtil;

public class MenuService {

	private MenuDAO menuDAO;
	private CompanyDAO companyDAO;
	private VideoDAO videoDAO;

	/**
	 * ��Ŀ����
	 * @param params
	 * 
	 * <pre>
	 *   loginUser.comId    Ĭ��-1
	 *   state              in(15,20)
	 *   sortBy             Ĭ�� listOrder asc
	 * </pre>
	 * 
	 * @return
	 * 
	 * <pre>
	 *   groupList
	 *   menuGroupCount
	 *   menuGroupMax
	 * </pre>
	 */
	public Map<String, Object> getMenuGroupList(QueryParams params) {
		Company company = companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		List<Group> groupList = this.menuDAO.findMenuGroupList(params);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("groupList", groupList);
		// ȥ��Ĭ�ϵ������˵�����
		result.put("menuGroupCount", company.getMenuGroupCount() >= 2 ? company.getMenuGroupCount() - 2 : company.getMenuGroupCount());
		result.put("menuGroupMax", company.getMenuGroupMax());
		return result;
	}

	/**
	 * ��ѯ�����˵���Ŀ
	 * @param params
	 * 
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * 
	 * @return
	 */
	public Map<String, Object> getMenuGroupEdit(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Group group = menuDAO.findGroup(params.getGroupId(), params.getLoginUser().getComId());
		if (group == null) {
			throw new PageNotFoundException();
		}
		result.put("formAction", "/menu/menu_group_edit_submit.htm");
		result.put("msg", "ok");
		result.put("group", group);
		return result;
	}

	/**
	 * �����Ŀʱ�����Ϣ
	 * @param params
	 * @return
	 * 
	 * <pre>
	 * menuGroup.full	������������
	 * addok	�������
	 * </pre>
	 */
	public Map<String, Object> getMenuGroupAdd(QueryParams params) {
		Company company = companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		// ��������Ĭ��ϵͳ�˵�
		Group group = new Group();
		group.setFold(true); //�۵�
		group.setListStyle(1); //�б���ʾ
		if (company.getMenuGroupCount() >= company.getMenuGroupMax() + 2) {
			result.put("msg", "full");
		} else {
			result.put("msg", "addok");//�ݲ�ʹ��
			result.put("groupId", params.getGroupId());
			result.put("formAction", "/menu/menu_group_add_submit.htm");
		}
		result.put("group", group);
		return result;
	}

	/**
	 * �����Ŀ
	 * @param menu
	 * @param loginUser
	 * @return
	 * 
	 * <pre>
	 * addSuccess
	 * operateFail
	 * </pre>
	 */
	public String addMenuGroup(Menu menu, LoginUser loginUser) {
//		int maxListOrder = menuDAO.findMaxListOrder("MenuGroup", menu.getComId(), 0);
//		menu.setListOrder(maxListOrder + 1);
		menu.setComId(loginUser.getComId());
		menu.setState(CN.STATE_WAIT);
		menu.setCreateTime(new DateUtil().getDateTime());
		menu.setModifyTime(new DateUtil().getDateTime());
		int count = menuDAO.addMenuGroup(menu);
		if (count > 0) {
			UpdateMap menuMap = new UpdateMap("CompanyExtra");
			menuMap.addField("menuGroupCount ", "+", 1);
			menuMap.addWhere("comId", menu.getComId());
			menuDAO.update(menuMap);
			menu.setModifyTime(new DateUtil().getDate());
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.ADD, "�����Ŀ����" + menu.getGroupName(), loginUser);
			companyDAO.addUserLog(userLog);
			return "addSuccess";
		} else {
			return "group.insert.error";
		}
	}

	/**
	 * �޸���Ŀ
	 * @param group
	 * 
	 * <pre>
	 * group.comId
	 * group.groupId
	 * </pre>
	 * 
	 * @param loginUser
	 * @return
	 * 
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateMenuGroup(Menu menu, LoginUser loginUser) {
		int comId = loginUser.getComId();
		menu.setComId(comId);
		int ret = menuDAO.findMenuGroupCountByGroupName(menu.getGroupName(), 
				comId, menu.getGroupId());
		if (ret > 0) {
			return "group.groupname.Repeat";
		} else {
			menu.setModifyTime(new DateUtil().getDate());
			UpdateMap updateMap = new UpdateMap("MenuGroup");
			updateMap.addField("groupName", menu.getGroupName());
			updateMap.addField("listStyle", menu.getListStyle());
			updateMap.addField("isFold", menu.isFold() ? 1 : 0);
			updateMap.addField("isShowDate", menu.isShowDate() ? 1 : 0);
			updateMap.addField("modifyTime", new DateUtil().getDateTime());
			
			Group mg = menuDAO.findGroup(menu.getGroupId(), comId);
			int oldState = mg.getState();
			int newState = 0; 
			if (loginUser.getMemberType() == 2) {
				if (oldState == CN.STATE_PASS) {
					newState = CN.STATE_REJECT_WAIT; // �������
				} else if (oldState == CN.STATE_REJECT_WAIT) { // ������� �޸ĺ��� 
					newState = CN.STATE_REJECT_WAIT;
				} else {
					newState = CN.STATE_WAIT;
				}
			} else {
				newState= CN.STATE_WAIT;
			}
			updateMap.addField("state", newState);
			
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", menu.getGroupId());
			int num = menuDAO.update(updateMap);
			if (num <= 0) {
				return "operateFail";
			}
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.MODIFY, 
					"�޸���Ŀ����" + menu.getGroupName(), loginUser);
			menuDAO.addUserLog(userLog);
			return "editSuccess";
		}
	}

	/**
	 * ɾ����Ŀ
	 * @param params
	 * 
	 * <pre>
	 * loginUser.comId
	 * groupId
	 * </pre>
	 * 
	 * @return
	 * 
	 * <pre>
	 * group.fixNotDel	����ɾ���̶���Ŀ
	 * group.menuCountNotDel	����ɾ������Ϣ����Ŀ
	 * deleteSuccess
	 * operateFail
	 * </pre>
	 */
	public String deleteMenuGroup(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Group group = menuDAO.findGroup(params.getGroupId(), comId);
		UpdateMap updateMap = new UpdateMap("MenuGroup");
		if (group.isFix()) {
			return "group.fixNotDel";
		} else if (group.getMenuCount() > 0) {
			return "group.menuCountNotDel";
		}
		updateMap.addField("listOrder", 0);
		updateMap.addField("menuCount", 0);
		updateMap.addField("menuRejectCount", 0);
		updateMap.addField("state", CN.STATE_DELETE);
		updateMap.addField("modifyTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("groupId", params.getGroupId());
		int num = menuDAO.update(updateMap);
		if (num > 0) {
			updateMap = new UpdateMap("CompanyExtra");
			updateMap.addField("menuGroupCount  ", "-", 1);
			updateMap.addWhere("comId", comId);
			menuDAO.update(updateMap);

			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.DELETE, 
					"ɾ����Ŀ����" + group.getGroupName(), params.getLoginUser());
			menuDAO.addUserLog(userLog);
			return "deleteSuccess";
		}
		return "operateFail";
	}

	/**
	 * ��Ϣ����
	 * @param params
	 * 
	 * <pre>
	 * loginUser.comId	
	 * groupId	Ĭ��-1
	 * state	in(10,14,15,20)
	 * pageNo	Ĭ��1
	 * queryBy	Ĭ��null
	 * queryText	Ĭ��null
	 * sortBy	Ĭ��listOrder��viewCount��modifyTime
	 * </pre>
	 * 
	 * @return
	 * 
	 * <pre>
	 * group
	 * listResult
	 * </pre>
	 */
	public Map<String, Object> getMenuList(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Group group = null; 
		if(params.getGroupId()!=0){
			 group = menuDAO.findGroup(params.getGroupId(), comId);
			if (group == null) {
				throw new PageNotFoundException();
			}
		}
		Map<String, Object> result = new HashMap<String, Object>();
		ListResult<Menu> listResult = menuDAO.findMenuList(params);
		result.put("group", group);
		result.put("listResult", listResult);
		return result;
	}
	
	public Map<String, Object> getMenuAdd(int comId, int groupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = companyDAO.findCompanyMemberType(comId);
		if (company == null) throw new PageNotFoundException();
		Group group = getGroup(groupId, company);
		if (group == null) throw new PageNotFoundException();
		
		result.put("formAction", "/menu/menu_add_submit.htm?groupId=" + group.getGroupId());
		result.put("group", group);
		result.put("isFull", group.getMenuCount() >= group.getItemMaxCount());
		result.put("isImgFull", company.getImgCount() >= company.getImgMax());
		return result;
	}

	/**
	 * ��Ӳ˵�ʱ�����Ϣ
	 * @param params
	 * @return
	 */
	public Map<String, Object> getMenuAdd(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = companyDAO.findCompanyMemberType(comId);
		Group group = getGroup(params.getGroupId(), company);
		if (company == null || group == null) {
			throw new PageNotFoundException();
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("formAction", "/menu/menu_add_submit.htm?groupId=" + params.getGroupId());
		result.put("group", group);
		result.put("isFull", group.getMenuCount() >= group.getItemMaxCount());
		result.put("isImgFull", company.getImgCount() >= company.getImgMax());
		return result;	
	}
	
	private Group getGroup(int groupId, Company company) {
		if (company == null) return null;
		Group group = this.menuDAO.findGroup(groupId, company.getComId());
		if (group == null) return null;
		group.setItemMaxCount(company.getMenuMax());
		return group;
	}
	
	/**
	 * ��ѯ�����˵���Ϣ
	 * @param loginUser.comId
	 * @param menuId
	 * @return menu
	 */
	public Map<String, Object> getMenuEdit(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		int comId = params.getLoginUser().getComId();
		Menu menu = menuDAO.findMenu(params.getMenuId(), comId);
		if (menu == null) {
			throw new PageNotFoundException();
		}
		Company company = this.companyDAO.findCompanyMemberType(comId);
		Group group = getGroup(menu.getGroupId(), company);
		if (company == null || group == null) {
			throw new PageNotFoundException();
		}
		result.put("formAction", "/menu/menu_edit_submit.htm?menuId=" + params.getMenuId() + "&groupId=" + menu.getGroupId());
		result.put("group", group);
		result.put("isImgFull", company.getImgCount() >= company.getImgMax());
		if (menu.getVideoId() > 0) {
			Video video = this.videoDAO.findVideo(comId, menu.getVideoId());
			if (video != null) {
				menu.setVideoImgPath(video.getImgPath());
				menu.setPlayPath(video.getPlayPath());
				menu.setVideoState(video.getState());
			}
		}
		
		result.put("menu", menu);
		return result;
	}

	/**
	 * ��Ӳ˵���Ϣ
	 * @param menu
	 * 
	 * <pre>
	 * comId
	 * groupId
	 * </pre>
	 * 
	 * @param loginUser
	 * @return
	 * 
	 * <pre>
	 * menu.count.limit	������������
	 * addSuccess
	 * operateFail
	 * </pre>
	 */
	public String addMenu(Menu menu, LoginUser loginUser) {
		int comId = loginUser.getComId();
		menu.setComId(comId);
		
		Company company = companyDAO.findCompanyMemberType(comId);
		Group group = menuDAO.findGroup(menu.getGroupId(), comId);
		if (group == null) {
			throw new PageNotFoundException();
		}
		if (group.getMenuCount() >= company.getMenuMax()) {
			return "menu.count.limit";
		}
//		int maxListOrder = menuDAO.findMaxListOrder("Menu", menu.getComId(), menu.getGroupId());
//		menu.setListOrder(maxListOrder + 1);
		menu.setState(CN.STATE_WAIT);
		menu.setCreateTime(new DateUtil().getDateTime());
		menu.setModifyTime(new DateUtil().getDateTime());
		Map<String, String> map = UploadUtil.getImgParam(menu.getImgPath());// ͼƬ
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				image.setComId(menu.getComId());
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Image.MENU);
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "menu.image.uploadFail";
				}
				menu.setImgId(img.getImgId());
				menu.setImgPath(img.getImgPath());
			}
		}
		// �ϴ�����
		if (menu.getAttachment() != null) {
			menu.setFilePath(this.getFilePath(menu));
		}

		int num = menuDAO.addMenu(menu);
		if (num > 0) {
			if (StringUtil.isNotEmpty(menu.getContent())) {
				menu.setMenuId(num);// ���ò˵���Ϣ����
				menuDAO.addMenuExtra(menu);// ��Ӳ˵���Ϣ���ݵ�MenuExtra��
			}
			UpdateMap menuMap = new UpdateMap("MenuGroup");
			menuMap.addField("menuCount", "+", 1);
			menuMap.addWhere("comId", comId);
			menuMap.addWhere("groupId", menu.getGroupId());
			menuDAO.update(menuMap);// ���Ӳ˵���Ŀ��Ӧ��˵���Ϣ����

			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.ADD, 
					"�����Ϣ����" + menu.getTitle(), loginUser);
			menuDAO.addUserLog(userLog);
			return "addSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * �޸Ĳ˵���Ϣ
	 * @param menu
	 * 
	 * <pre>
	 * menuId
	 * groupId
	 * </pre>
	 * 
	 * @param loginUser
	 * @return
	 * 
	 * <pre>
	 * editSuccess
	 * operateFail
	 * </pre>
	 */
	public String updateMenu(Menu menu, LoginUser loginUser) {
		int comId = loginUser.getComId();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		menu.setComId(comId);
		Menu oldMenu = menuDAO.findMenu(menu.getMenuId(), comId);
		UpdateMap menuMap = new UpdateMap("MenuGroup");
		if (oldMenu.getState() == CN.STATE_REJECT) {
			menuMap.addField("menuRejectCount ", "-", 1);
			menuMap.addWhere("groupId", menu.getGroupId());
			menuDAO.update(menuMap);
		}
		Map<String, String> map = UploadUtil.getImgParam(menu.getImgPath());// ͼƬ
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() < company.getImgMax()) {
				Image image = new Image();
				image.setComId(comId);
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "menu.image.uploadFail";
				}
				menu.setImgId(img.getImgId());
				menu.setImgPath(img.getImgPath());
			}
		}
		// �ϴ�����
		if (menu.getAttachment() != null) {
			menu.setFilePath(this.getFilePath(menu));
		}
		
		StringUtil.trimToEmpty(menu, "filePath");
		
		menuMap = new UpdateMap("Menu");
		menuMap.addField("title", menu.getTitle());
		menuMap.addField("imgId", menu.getImgId());
		menuMap.addField("imgPath", menu.getImgPath());
		menuMap.addField("videoId", menu.getVideoId());
		menuMap.addField("filePath", menu.getFilePath());
		
		Menu mn = menuDAO.findMenu(menu.getMenuId(), comId);
		int oldState = mn.getState();
		int newState = 0; 
		if (loginUser.getMemberType() == 2) {
			if (oldState == CN.STATE_PASS) {
				newState = CN.STATE_REJECT_WAIT; // �������
			} else if (oldState == CN.STATE_REJECT_WAIT) { // ������� �޸ĺ��� 
				newState = CN.STATE_REJECT_WAIT;
			} else {
				newState = CN.STATE_WAIT;
			}
		} else {
			newState= CN.STATE_WAIT;
		}
		menuMap.addField("state", newState);
		
		menuMap.addField("modifyTime", new DateUtil().getDateTime());
		menuMap.addWhere("comId", comId);
		menuMap.addWhere("menuId", menu.getMenuId());
		if (menuDAO.update(menuMap) > 0) {
			menuMap = new UpdateMap("MenuExtra");
			menuMap.addField("content", menu.getContent());
			menuMap.addWhere("comId", comId);
			menuMap.addWhere("menuId", menu.getMenuId());
			menuDAO.update(menuMap);

			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.MODIFY, 
					"�޸���Ϣ����" + menu.getTitle(), loginUser);
			menuDAO.addUserLog(userLog);
			return "editSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ɾ���˵���Ϣ
	 * @param menuId
	 * @param comId
	 * @return deleteSuccess operateFail
	 */
	public String deleteMenu(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Menu menu = menuDAO.findMenu(params.getMenuId(), comId);
		UpdateMap updateMap = new UpdateMap("MenuGroup");
		if (menu.getState() == CN.STATE_REJECT) {
			updateMap.addField("menuCount ", "-", 1);
			updateMap.addField("menuRejectCount ", "-", 1);
			updateMap.addWhere("groupId", menu.getGroupId());
			menuDAO.update(updateMap);
		} else {
			updateMap.addField("menuCount ", "-", 1);
			updateMap.addWhere("groupId", menu.getGroupId());
			menuDAO.update(updateMap);
		}
		updateMap = new UpdateMap("Menu");
		updateMap.addField("videoId", 0);
		updateMap.addField("imgId", 0);
		updateMap.addField("state", CN.STATE_DELETE);
		updateMap.addField("modifyTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("menuId", menu.getMenuId());
		int num = menuDAO.update(updateMap);
		if (num > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.MENU, UserLog.DELETE, 
					"ɾ����Ϣ����" + menu.getTitle(), params.getLoginUser());
			menuDAO.addUserLog(userLog);
			return "deleteSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ���²˵���Ϣ����
	 * @param params
	 * 
	 * <pre>
	 * loginUser.comId
	 * groupIds
	 * </pre>
	 * 
	 * @return
	 * 
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateMenuOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int[] menuIds = params.getMenuIds();
		int listOrder = 1;

		if (menuIds.length > 0) {
			List<UpdateMap> list = new ArrayList<UpdateMap>();
			for (int i = 0; i < menuIds.length; i++) {
				UpdateMap updateMap = new UpdateMap("Menu");
				updateMap.addField("listOrder", listOrder);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("menuId", menuIds[i]);
				list.add(i, updateMap);
				listOrder++;
			}
			int num = menuDAO.update(list);
			if (num <= 0) {
				return "operateFail";
			}
		} else {
			return "operateFail";
		}

		return "orderSuccess";
	}

	/**
	 * ���²˵���Ŀ����
	 * @param params
	 * 
	 * <pre>
	 * loginUser.comId
	 * groupIds
	 * </pre>
	 * 
	 * @return
	 * 
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateMenuGroupOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int[] groupIds = params.getGroupIds();
		int listOrder = 1;

		if (groupIds == null || groupIds.length == 0) {
			return "operateFail";
		}

		List<UpdateMap> list = new ArrayList<UpdateMap>();
		for (int i = 0; i < groupIds.length; i++) {
			UpdateMap updateMap = new UpdateMap("MenuGroup");
			updateMap.addField("listOrder", listOrder);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", groupIds[i]);
			list.add(i, updateMap);
			listOrder++;
		}
		int num = menuDAO.update(list);
		if (num <= 0) {
			return "operateFail";
		}
		return "orderSuccess";
	}
	
	public Map<String, Object> getMenuSubmitError(Menu menu, HttpServletRequest request) {
		Ticket ticket = CASClient.getInstance().getUser(request);
		if (ticket == null) throw new PageNotFoundException();
		
		Map<String, Object> result = getMenuAdd(ticket.getComId(), menu.getGroupId());
		result.put("menu", menu);
		return result;
	}

	/**
	 * ����ϴ�����·��
	 * @param menu
	 * @return
	 */
	private String getFilePath(Menu menu) {
		try {
			Attachment att = UploadUtil.uploadFileStream(menu.getComId(), 
					2, menu.getAttachmentFileName(), 
					new FileInputStream(menu.getAttachment()));
			if (att != null) {
				menu.setFilePath(att.getFilePath().replace("\\", "/"));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return menu.getFilePath();
	}

	public void setMenuDAO(MenuDAO menuDAO) {
		this.menuDAO = menuDAO;
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}
}
