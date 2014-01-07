/**
 * Created by wuyaohui at 2013-1-21 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.member;

import java.util.ArrayList;
import java.util.List;

import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Talk;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.util.StringUtil;

/**
 * @author wuyaohui
 *
 */
public class TalkService {
	
	private TalkDAO talkDAO;
	
	
	public List<Talk> getTalks(int userId) {
		return talkDAO.findTalks(userId);
	}
	
	public void deleteTalksByUserId(int userId) {
		UpdateMap map = new UpdateMap("Talk");
		map.addWhere("userId", userId);
		talkDAO.delete(map);
	}
	
	private void addTalks(List<Talk> talks, int userId) {
		// clear user talks
		deleteTalksByUserId(userId);
		
		// add talks
		talkDAO.batchAdd(talks);
	}

	/**
	 * @param user
	 * @param loginu
	 */
	public boolean addTalks(User user, int comId) {
		List<Talk> talks = new ArrayList<Talk>();
		boolean result = true;
		int userId = user.getUserId();
		// ÅÐ¶ÏÊÇ·ñÆóÒµQQ
		if (user.isQq_type()) {
			Talk bigQQ = new Talk();
			bigQQ.setCode(user.getBigqq_id());
			bigQQ.setName(user.getBigqq_name());
			bigQQ.setComId(comId);
			bigQQ.setUserId(userId);
			bigQQ.setType(Talk.BIGQQ);
			talks.add(bigQQ);
			if (bigQQ.isValidate()) {
				bigQQ.setId(bigQQ.generateId(0));
				user.setQq(bigQQ.getCode());
			} else {
				result = false;
			}
		} else {
			if (user.getQq_id() == null || user.getQq_name() == null) {
				return false;
			}
			int count = user.getQq_id().length;
			if (count > 0 && count == user.getQq_name().length) {
				Talk talk = null;
				String name = null, id = null;
				
				for (int i = 0; i < count; i++) {
					name = user.getQq_name()[i];
					id = user.getQq_id()[i];
					if (StringUtil.isEmpty(name) && StringUtil.isEmpty(id)) {
						continue;
					}
					if (i == 0) user.setQq(id);
					
					talk = new Talk();
					talk.setName(user.getQq_name()[i]);
					talk.setCode(user.getQq_id()[i]);
					talk.setUserId(userId);
					talk.setComId(comId);
					talk.setType(Talk.QQ);
					talks.add(talk);
					if (talk.isValidate()) {
						talk.setId(talk.generateId(i));
					} else {
						result = false;
						break;
					}
				}
			}
		}
		if (!result) {
			user.setTalks(talks);
			return false;
		}
		if (talks.isEmpty()) {
			deleteTalksByUserId(user.getUserId());
		} else {
			addTalks(talks, userId);
		}
		return true;
	}
	
	public void setTalkDAO(TalkDAO talkDAO) {
		this.talkDAO = talkDAO;
	}
}
