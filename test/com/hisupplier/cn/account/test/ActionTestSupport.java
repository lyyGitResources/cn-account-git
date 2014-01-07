package com.hisupplier.cn.account.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import com.hisupplier.cas.Constants;
import com.hisupplier.cas.Ticket;
import com.hisupplier.cn.account.alert.TradeAlertAction;
import com.hisupplier.cn.account.basic.CASClient;
import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.entity.Comment;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Menu;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.TradeAlert;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.entity.Vote;
import com.hisupplier.cn.account.group.GroupAction;
import com.hisupplier.cn.account.group.SpecialGroupAction;
import com.hisupplier.cn.account.inquiry.InquiryAction;
import com.hisupplier.cn.account.member.CompanyAction;
import com.hisupplier.cn.account.member.UserAction;
import com.hisupplier.cn.account.menu.MenuAction;
import com.hisupplier.cn.account.misc.CommentAction;
import com.hisupplier.cn.account.misc.ImageAction;
import com.hisupplier.cn.account.misc.VideoAction;
import com.hisupplier.cn.account.misc.VideoGroupAction;
import com.hisupplier.cn.account.misc.VoteAction;
import com.hisupplier.cn.account.product.NewProductAction;
import com.hisupplier.cn.account.product.ProductAction;
import com.hisupplier.cn.account.product.TradeAction;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.Global;
import com.hisupplier.commons.basket.Basket;
import com.hisupplier.commons.basket.BasketFactory;
import com.hisupplier.commons.entity.cn.Group;
import com.hisupplier.commons.entity.cn.Inquiry;
import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.mail.MailTemplate;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.struts2.ActionTestCase;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.CategoryUtil;

public class ActionTestSupport extends ActionTestCase {

	/**
	 * 初始化测试环境
	 */
	static {
		initLog4j("/log4j.xml");

		org.apache.commons.dbcp.BasicDataSource jdbc_hisupplier = new org.apache.commons.dbcp.BasicDataSource();
		jdbc_hisupplier.setDriverClassName("com.mysql.jdbc.Driver");
		jdbc_hisupplier.setUrl("jdbc:mysql://192.168.1.5:3309/hisupplier_cn?useUnicode=true&amp;characterEncoding=UTF-8");
		jdbc_hisupplier.setUsername("root");
		jdbc_hisupplier.setPassword("lifeblood");

		org.apache.commons.dbcp.BasicDataSource jdbc_sms = new org.apache.commons.dbcp.BasicDataSource();
		jdbc_sms.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		jdbc_sms.setUrl("jdbc:jtds:sqlserver://61.164.79.45:65335/HSSMS");
		jdbc_sms.setUsername("hsdba");
		jdbc_sms.setPassword("SMS4HS#2008");

		System.setProperty(Global.DEBUG, "true");
		System.setProperty("com.hisupplier.commons.global.debug.email", "rd20@hi.cc");

		JdbcUtilFactory.init(jdbc_hisupplier);
		TaskExecutor.init("cn-account", true);
		MailSenderCNFactory.init();
		MailSenderCNFactory.setDebug(false);
		//MasSMSSender.init();
		Config.init("account");
		CategoryUtil.init();
		CN.initMemberType();
		//初始化邮件模板工厂
		try {
			MailTemplate.init("E:/workspace/cn-account-3.x/WebRoot/page/template/mail");
		} catch (IOException e) {
			e.printStackTrace();
		}
		//初始化单点登录客户端
		try {
			CASClient.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		initSpring();
	}

	/**
	 * 
	 */
	public ActionTestSupport() {
		super();

		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		session = new MockHttpSession();
		request.setSession(session);
		ServletActionContext.setRequest(request);
		ServletActionContext.setResponse(response);
		this.setLogin();
	}

	/**
	 * 在每个测试方法执行前调用
	 */
	public void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 在每个测试方法执行后调用
	 */
	public void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 设置为登录状态，帐号guiyou
	 */
	public void setLogin() {
		User user = this.getUser();
		Ticket ticket = new Ticket();
		ticket.setComId(user.getComId());
		ticket.setUserId(user.getUserId());
		LoginUser loginUser = (LoginUser) CASClient.getInstance().login(request, response, ticket);
		request.setAttribute("com.hisupplier.cas.login", true);
		request.getSession().setAttribute(Constants.LOGIN_USER, loginUser);
	}

	/**
	 * 设置为未登录状态
	 */
	public void setNotLogin() {
		request.setAttribute("com.hisupplier.cas.login", false);
		request.getSession().setAttribute(Constants.LOGIN_USER, null);
	}

	/**
	 * 设置重复提交key
	 */
	@SuppressWarnings("unchecked")
	public void setValidateToken() {
		String token = TokenHelper.generateGUID();
		ServletActionContext.getContext().getParameters().put(TokenHelper.TOKEN_NAME_FIELD, new String[] { TokenHelper.DEFAULT_TOKEN_NAME });
		ServletActionContext.getContext().getParameters().put(TokenHelper.DEFAULT_TOKEN_NAME, new String[] { token });
		ServletActionContext.getContext().getSession().put(TokenHelper.DEFAULT_TOKEN_NAME, token);
	}

	/**
	 * 设置10条随机的产品数据到询盘篮中
	 */
	public void setBasketItems() {
		JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
		try {
			List<String> productList = new ArrayList<String>();
			jd.query("select * from Product order by proId desc limit 10");
			while (jd.resultNext()) {
				productList.add(jd.getInt("proId") + Basket.VALUE_SPLIT + jd.getInt("comId"));
			}
			request.setParameter(Basket.PRODUCT, productList.toArray(new String[productList.size()]));
		} finally {
			jd.close();
		}

		Basket basket = BasketFactory.getBasket(request, "cn");
		basket.addItems(request, response);
	}

	/**
	 * 从数据库中随机查询一个用户
	 * @return
	 */
	public User getUser() {
		User user = null;
		JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
		try {
			jd.query("select * from Users order by userId desc limit 1");
			while (jd.resultNext()) {
				user = new User();
				user.setUserId(jd.getInt("userId"));
				user.setComId(jd.getInt("comId"));
				user.setEmail(jd.getString("email"));
				user.setPasswd(jd.getString("passwd"));
			}

		} finally {
			jd.close();
		}
		return user;
	}

	public Company getCompanyEdit() throws Exception {
		CompanyAction action = createAction(CompanyAction.class, "/member", "company_edit");

		proxy.setMethod("company_edit");
		proxy.execute();

		return (Company) action.getResult().get("company");
	}

	public User getContactEdit() throws Exception {
		UserAction action = createAction(UserAction.class, "/member", "contact_edit");
		proxy.setMethod("contact_edit");
		proxy.execute();

		return (User) action.getResult().get("user");
	}

	@SuppressWarnings("unchecked")
	public User getUserEdit() throws Exception {
		UserAction action = createAction(UserAction.class, "/member", "user_list");
		proxy.setMethod("user_list");
		proxy.execute();
		User user = null;
		List<User> list = (List<User>) action.getResult().get("userList");
		if (list != null) {
			if (list.size() > 0) {
				user = list.get(0);
			}
		}
		return user;
	}

	@SuppressWarnings("unchecked")
	protected Inquiry getInquiry(int state) throws Exception {
		InquiryAction action = createAction(InquiryAction.class, "/inquiry", "inquiry_list");

		com.hisupplier.cn.account.inquiry.QueryParams params = action.getModel();
		params.setPageSize(20);
		params.setState(state);
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("inquiry_list");
		proxy.execute();

		Inquiry inquiry = null;
		ListResult<Inquiry> listResult = (ListResult<Inquiry>) action.getResult().get("inquiryList");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				inquiry = listResult.getList().get(0);
			}
		}
		return inquiry;
	}

	@SuppressWarnings("unchecked")
	protected Inquiry getInquiryRecycle() throws Exception {
		InquiryAction action = createAction(InquiryAction.class, "/inquiry", "inquiry_recycle_list");

		com.hisupplier.cn.account.inquiry.QueryParams params = action.getModel();
		params.setPageSize(1);
		params.setState(CN.STATE_RECYCLE);
		proxy.setMethod("inquiry_recycle_list");
		proxy.execute();

		Inquiry inquiry = null;
		ListResult<Inquiry> listResult = (ListResult<Inquiry>) action.getResult().get("inquiryList");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				inquiry = listResult.getList().get(0);
			}
		}
		return inquiry;
	}

	@SuppressWarnings("unchecked")
	protected Comment getComment(String commentType) throws Exception {
		CommentAction action = createAction(CommentAction.class, "/comment", "comment_list");

		com.hisupplier.cn.account.misc.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		params.setPageSize(15);
		params.setCommentType(commentType);
		proxy.setMethod("comment_list");
		proxy.execute();

		Comment comment = null;
		ListResult<Comment> listResult = (ListResult<Comment>) action.getResult().get("listResult");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				comment = listResult.getList().get(0);
			}
		}
		return comment;
	}

	@SuppressWarnings("unchecked")
	protected Vote getVote() throws Exception {
		VoteAction action = createAction(VoteAction.class, "/vote", "vote_list");

		com.hisupplier.cn.account.misc.QueryParams params = action.getModel();
		params.setPageSize(15);
		proxy.setMethod("vote_list");
		proxy.execute();

		Vote vote = null;
		ListResult<Vote> listResult = (ListResult<Vote>) action.getResult().get("listResult");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				vote = listResult.getList().get(0);
			}
		}
		return vote;
	}

	@SuppressWarnings("unchecked")
	protected Group getGroup() throws Exception {
		VideoGroupAction action = createAction(VideoGroupAction.class, "/video", "video_group_list");

		com.hisupplier.cn.account.misc.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		params.setPageSize(15);
		proxy.setMethod("video_group_list");
		proxy.execute();

		Group group = null;
		ListResult<Group> listResult = (ListResult<Group>) action.getResult().get("listResult");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				group = listResult.getList().get(0);
			}
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	protected Video getVideo() throws Exception {
		VideoAction action = createAction(VideoAction.class, "/video", "video_list");

		com.hisupplier.cn.account.misc.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		params.setPageSize(15);
		proxy.setMethod("video_list");
		proxy.execute();

		Video video = null;
		ListResult<Video> listResult = (ListResult<Video>) action.getResult().get("listResult");

		if (listResult != null) {
			if (listResult.getList().size() > 0) {
				video = listResult.getList().get(0);
			}
		}
		return video;
	}

	@SuppressWarnings("unchecked")
	protected TradeAlert getTradeAlert() throws Exception {
		TradeAlertAction action = createAction(TradeAlertAction.class, "/alert", "trade_alert_list");
		proxy.setMethod("trade_alert_list");
		proxy.execute();

		TradeAlert alert = null;
		List<TradeAlert> tradeAlertList = (List<TradeAlert>) action.getResult().get("alertList");
		if (tradeAlertList.size() > 0) {
			alert = tradeAlertList.get(tradeAlertList.size() - 1);
		}
		return alert;
	}

	@SuppressWarnings("unchecked")
	protected com.hisupplier.cn.account.entity.Group getProductGroup(boolean noChild, boolean noParent) throws Exception {
		GroupAction action = createAction(GroupAction.class, "/group", "group_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("group_list");
		proxy.execute();

		com.hisupplier.cn.account.entity.Group group = null;
		List<com.hisupplier.cn.account.entity.Group> list = (ArrayList<com.hisupplier.cn.account.entity.Group>) action.getResult().get("groupList");

		if (list != null && list.size() > 0) {
			if (noChild || noParent) {
				for (int i = 0; i < list.size(); i++) {
					if (noChild) {
						if (list.get(i).getChild() <= 0) {
							group = list.get(i);
							break;
						}
					} else {
						if (list.get(i).getParentId() <= 0) {
							group = list.get(i);
							break;
						}
					}
				}
			} else {
				group = list.get(0);
			}
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	protected Group getSpecialGroup() throws Exception {
		SpecialGroupAction action = createAction(SpecialGroupAction.class, "/specialGroup", "group_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("group_list");
		proxy.execute();

		Group group = null;
		List<Group> list = (ArrayList<Group>) action.getResult().get("groupList");

		if (list != null) {
			if (list.size() > 0) {
				group = list.get(0);
			}
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	protected Image getImage() throws Exception {
		ImageAction action = createAction(ImageAction.class, "/image", "image_list");

		com.hisupplier.cn.account.misc.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("image_list");
		proxy.execute();

		Image image = null;
		ListResult<Image> list = (ListResult<Image>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 0) {
			image = list.getList().get(0);
		}
		return image;
	}

	@SuppressWarnings("unchecked")
	protected Product getProduct(boolean haveGroupId) throws Exception {
		ProductAction action = createAction(ProductAction.class, "/product", "product_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("product_list");
		proxy.execute();

		Product product = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 0) {
			if (haveGroupId) {
				for (int i = 0; i < list.getList().size(); i++) {
					if (list.getList().get(i).getGroupId() > 0) {
						product = list.getList().get(i);
						break;
					}
				}
			} else {
				product = list.getList().get(0);
			}
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	protected int[] getProductId(boolean haveGroupId) throws Exception {
		ProductAction action = createAction(ProductAction.class, "/product", "product_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("product_list");
		proxy.execute();

		int[] proId = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			proId = new int[2];
			if (haveGroupId) {
				for (int i = 0; i < list.getList().size(); i++) {
					if (list.getList().get(i).getGroupId() > 0) {
						if (proId[0] <= 0) {
							proId[0] = list.getList().get(i).getProId();
						} else if (proId[1] <= 0) {
							proId[1] = list.getList().get(i).getProId();
						} else {
							break;
						}
					}
				}
			} else {
				proId[0] = list.getList().get(0).getProId();
				proId[1] = list.getList().get(1).getProId();
			}
		}
		return proId;
	}

	@SuppressWarnings("unchecked")
	protected Product getNewProduct() throws Exception {
		NewProductAction action = createAction(NewProductAction.class, "/newproduct", "new_product_list");
		proxy.setMethod("new_product_list");
		proxy.execute();

		Product product = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 0) {
			product = list.getList().get(0);
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	protected int[] getNewProductId() throws Exception {
		NewProductAction action = createAction(NewProductAction.class, "/newproduct", "new_product_list");

		proxy.setMethod("new_product_list");
		proxy.execute();

		int[] proId = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			proId = new int[2];
			proId[0] = list.getList().get(0).getProId();
			proId[1] = list.getList().get(1).getProId();
		}
		return proId;
	}

	@SuppressWarnings("unchecked")
	protected Product getTrade() throws Exception {
		TradeAction action = createAction(TradeAction.class, "/trade", "trade_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("trade_list");
		proxy.execute();

		Product product = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 0) {
			product = list.getList().get(0);
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	protected int[] getTradeId() throws Exception {
		TradeAction action = createAction(TradeAction.class, "/trade", "trade_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("trade_list");
		proxy.execute();

		int[] proId = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			proId = new int[2];
			proId[0] = list.getList().get(0).getProId();
			proId[1] = list.getList().get(1).getProId();
		}
		return proId;
	}

	@SuppressWarnings("unchecked")
	protected com.hisupplier.cn.account.entity.Group getMenuGroup(boolean haveMenu) throws Exception {
		MenuAction action = createAction(MenuAction.class, "/menu", "menu_group_list");
		com.hisupplier.cn.account.menu.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("menu_group_list");
		proxy.execute();
		com.hisupplier.cn.account.entity.Group group = null;
		List<com.hisupplier.cn.account.entity.Group> list = (List<com.hisupplier.cn.account.entity.Group>) action.getResult().get("groupList");
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				group = list.get(i);
				if (haveMenu) {
					break;
				}
				if (group.getMenuCount() == 0 && !group.isFix()) {
					break;
				}
			}
		}
		return group;
	}

	@SuppressWarnings("unchecked")
	protected Menu getMenu(int groupId) throws Exception {
		MenuAction action = createAction(MenuAction.class, "/menu", "menu_list");
		com.hisupplier.cn.account.menu.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		params.setGroupId(groupId);
		proxy.setMethod("menu_list");
		proxy.execute();

		ListResult<Menu> list = (ListResult<Menu>) action.getResult().get("listResult");
		Menu menu = null;
		if (list.getList().size() > 0) {
			menu = list.getList().get(0);
		}
		return menu;
	}

	@SuppressWarnings("unchecked")
	protected int[] getRecycleTradeId() throws Exception {
		TradeAction action = createAction(TradeAction.class, "/trade", "trade_recycle_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("trade_recycle_list");
		proxy.execute();

		int[] proId = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			proId = new int[2];
			proId[0] = list.getList().get(0).getProId();
			proId[1] = list.getList().get(1).getProId();
		}
		return proId;
	}

	@SuppressWarnings("unchecked")
	protected Product getNoFeatureProduct() throws Exception {
		ProductAction action = createAction(ProductAction.class, "/product", "no_feature_product_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("no_feature_product_list");
		proxy.execute();

		Product product = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			product = list.getList().get(0);
		}
		return product;
	}

	@SuppressWarnings("unchecked")
	protected Product getFeatureProduct() throws Exception {
		ProductAction action = createAction(ProductAction.class, "/product", "feature_product_list");

		com.hisupplier.cn.account.product.QueryParams params = action.getModel();
		LoginUser loginUser = new LoginUser();
		loginUser.setComId(442);
		params.setLoginUser(loginUser);
		proxy.setMethod("feature_product_list");
		proxy.execute();

		Product product = null;
		ListResult<Product> list = (ListResult<Product>) action.getResult().get("listResult");

		if (list.getList() != null && list.getList().size() > 1) {
			product = list.getList().get(0);
		}
		return product;
	}

}
