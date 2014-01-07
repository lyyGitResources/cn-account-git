package com.hisupplier.cn.account.basic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisupplier.cas.Ticket;
import com.hisupplier.commons.jdbc.JdbcUtil;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;

public class CASClient extends com.hisupplier.cas.CASClient {

	private static Log log = LogFactory.getLog(CASClient.class);

	public CASClient() {
	}

	@Override
	public LoginUser login(HttpServletRequest request, HttpServletResponse response, Ticket ticket) {
		LoginUser loginUser = null;
		JdbcUtil jd = JdbcUtilFactory.getInstance().getJdbcUtil();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select Company.memberType,Company.memberId,Company.keywords,Company.comName,Company.websites,Company.state,CompanyExtra.statSite,WebSite.domain,WebSite.domainEN ");
			sql.append(",Company.crmState from Company ");
			sql.append("left join CompanyExtra on CompanyExtra.comId = Company.comId ");
			sql.append("left join WebSite on WebSite.comId = Company.comId ");
			sql.append("where Company.comId = " + ticket.getComId());
			jd.query(sql.toString());
			if (jd.resultNext()) {
				loginUser = new LoginUser();
				loginUser.setDomain(jd.getString("domain"));
				loginUser.setMemberId(jd.getString("memberId"));
				loginUser.setKeywords(jd.getString("keywords"));
				loginUser.setMemberType(jd.getInt("memberType"));
				loginUser.setComName(jd.getString("comName"));
				loginUser.setWebsites(jd.getString("websites"));
				loginUser.setState(jd.getInt("state"));
				loginUser.setStatSite(jd.getString("statSite"));
				loginUser.setDomainEN(jd.getString("domainEN"));
				loginUser.setComCrmState(jd.getInt("crmState"));
				loginUser.setComId(ticket.getComId());
				loginUser.setUserId(ticket.getUserId());

				jd.query("select privilege,lastLoginIP,isAdmin,contact,email,userId,tel,mobile,fax,province,city,town,street,sex,crmState,state,parentId from Users where userId=" + ticket.getUserId());
				if (jd.resultNext()) {
					loginUser.setPrivilege(jd.getString("privilege"));
					loginUser.setLastLoginIP(jd.getString("lastLoginIP"));
					loginUser.setAdmin(jd.getBoolean("isAdmin"));
					loginUser.setContact(jd.getString("contact"));
					loginUser.setEmail(jd.getString("email"));
					loginUser.setUserId(jd.getInt("userId"));
					loginUser.setTel(jd.getString("tel"));
					loginUser.setMobile(jd.getString("mobile"));
					loginUser.setFax(jd.getString("fax"));
					loginUser.setProvince(jd.getString("province"));
					loginUser.setCity(jd.getString("city"));
					loginUser.setTown(jd.getString("town"));
					loginUser.setStreet(jd.getString("street"));
					loginUser.setSex(jd.getInt("sex"));
					loginUser.setUserCrmState(jd.getInt("crmState"));
					loginUser.setUserState(jd.getInt("state"));
					loginUser.setParentId(jd.getInt("parentId"));
				}
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			JdbcUtilFactory.getInstance().closeJdbcUtil(jd);
		}
		return loginUser;
	}

}
