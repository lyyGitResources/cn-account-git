package com.hisupplier.cn.account.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import com.hisupplier.cas.CASClient;
import com.hisupplier.cn.account.entity.AdOrder;
import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.entity.InquiryReply;
import com.hisupplier.cn.account.entity.Register;
import com.hisupplier.cn.account.entity.ServiceMail;
import com.hisupplier.cn.account.entity.TopOrder;
import com.hisupplier.cn.account.entity.UpgradeMail;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.commons.Config;
import com.hisupplier.commons.Global;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.jdbc.JdbcUtilFactory;
import com.hisupplier.commons.mail.Mail;
import com.hisupplier.commons.mail.MailSender;
import com.hisupplier.commons.mail.MailSenderCNFactory;
import com.hisupplier.commons.mail.MailTemplate;
import com.hisupplier.commons.task.TaskExecutor;
import com.hisupplier.commons.util.CategoryUtil;

public class MailFactoryTest extends TestCase {

	static {
		System.setProperty(Global.DEBUG, "true");
		System.setProperty(Global.DEBUG + ".email", "247018028@qq.com");

		MailSenderCNFactory.init();
		MailSenderCNFactory.setDebug(true);
		TaskExecutor.init("cn-account", true);

		org.apache.commons.dbcp.BasicDataSource ds = new org.apache.commons.dbcp.BasicDataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		ds.setUrl("jdbc:mysql://192.168.1.5:3309/hisupplier_cn?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull");
//		ds.setUrl("jdbc:mysql://localhost:3306/hisupplier?useUnicode=true&amp;characterEncoding=UTF-8&amp;zeroDateTimeBehavior=convertToNull");
		ds.setUsername("root");
		ds.setPassword("developer");

		JdbcUtilFactory.init(ds);
		JdbcUtilFactory.getInstance().setDataSource(ds);
//		CategoryUtil.init();
	}

	@Override
	protected void setUp() throws Exception {
		CASClient.init();
	}

	@Override
	protected void tearDown() throws Exception {
		//TimeUnit.SECONDS.sleep(5);
	}

	public MailFactoryTest() throws IOException {
		Config.setString("sys.base", "http://cn.jiaming.com");
		Config.setString("sys.domain", "jiaming.com");
		Config.setString("account.base","http://account.cn.jiaming.com");
		Config.setString("cas.clientImpl", "com.hisupplier.cas.CASClient");
		MailTemplate.init("F:/wyh/workspace/cn-account-3.5.x/WebRoot/page/template/mail");
	}
	/**
	 * 主站正常注册
	 */
	public void testGetJoinRegister() throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regMode", 1);
		params.put("editMemberId", "false");
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "yaozhan189@163.com");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getRegister(params);
		mail.setCarbonCopyTo("yaozhan189@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "register.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}

	/**
	 * 发布求购信息注册
	 */
	public void testGetBuyerRegister() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regMode", 4);
		params.put("editMemberId", "false");
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "601552529@qq.com");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getRegister(params);
		mail.setCarbonCopyTo("miaonihao521@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "register.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	
	/**
	 * 商情订阅注册(关键词订阅)
	 */
	public void testGetAlertRegister() throws Exception {
		
		Register register = new Register();
		register.setTradeAlertKeyword("笔");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regMode", 5);
		params.put("tradeAlertKeyword", register.getTradeAlertKeyword());
		params.put("tradeAlertCatName", "");
		params.put("editMemberId", "false");
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "601552529@qq.com");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getRegister(params);
		mail.setCarbonCopyTo("miaonihao521@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "register.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	/**
	 * 商情订阅注册(目录订阅)
	 */
	public void testGetAlertRegister1() throws Exception {
		Register register = new Register();
		register.setCatIds("41");
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regMode", 5);
		params.put("tradeAlertKeyword", "");
		params.put("tradeAlertCatName", CategoryUtil.getNamePath(Integer.parseInt(register.getCatIds()), ">>"));
		params.put("editMemberId", "false");
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "601552529@qq.com");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getRegister(params);
		mail.setCarbonCopyTo("miaonihao521@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "register.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	
	/**
	 * 询盘注册
	 */
	public void testGetInquiryRegister() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("regMode", 6);
		params.put("editMemberId", "false");
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "601552529@qq.com");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getRegister(params);
		mail.setCarbonCopyTo("miaonihao521@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "register.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}

	public void testGetForgetPasswd() throws Exception {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("contact", "sdfdsf");
		params.put("memberId", "sdf");
		params.put("passwd", "sdf");
		params.put("email", "601552529@qq.com");
		params.put("editMemberId", "false");
		params.put("service_contact", CASClient.getInstance().getAutoLoginURL(Config.getString("account.base") + "/basic/service_mail.htm", "sdf", "sdf"));
		params.put("service_email", MailFactory.SERVICE_EMAIL);
		Mail mail = MailFactory.getForgetPasswd(params);
		mail.setCarbonCopyTo("miaonihao521@163.com");
		MailTemplate.writeHtml("E:/MailTemplate", "forget_passwd.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}

	public void testGetServiceMail() throws Exception {
		ServiceMail serviceMail = new ServiceMail();
		serviceMail.setToEmail("linliuwei3@163.com");
		serviceMail.setToName("aaaas");
		serviceMail.setComName("asd");
		serviceMail.setContact("asd");
		serviceMail.setContactMode("sssss");
		serviceMail.setSubject("asd");
		serviceMail.setContent("asdasd");
		serviceMail.setEmail("asdads");
		serviceMail.setTel("asdasd");
		serviceMail.setFax("asdads");
		serviceMail.setMemberId("asdads");
		Mail mail = MailFactory.getServiceMail(serviceMail);
		MailTemplate.writeHtml("E:/MailTemplate", "service_mail.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}

	public void testGetInquiryReply() throws Exception {
		InquiryReply inquiryReply = new InquiryReply();
		inquiryReply.setToEmail("yaozhan189@163.com");
		inquiryReply.setToName("aaaas");
		inquiryReply.setFromEmail("yaozhan189@163.com");
		inquiryReply.setFromName("aaa111");
		inquiryReply
				.setFromContent(" <!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>  <html xmlns='http://www.w3.org/1999/xhtml'>  <head>  <title>询盘信息</title>  <style type='text/css'>  body { margin: 0px;padding: 0px;font:12px Arial ;line-height: 140%;color: #333;background-color: #fff;}  a:link,a:visited {color:#0000ff;text-decoration: underline;}  #web {width:600px;margin:0 auto;overflow:hidden;padding:0 10px;}  #top {height:65px;padding:15px 0;}  #top2 {float:right;width:300px;margin-top:68px;font: bold 20px Arial;}  .fgx {width:595px; height:20px; text-align:right; color:#fff; background: url(http://cn.hisupplier.com/images/m_bg.jpg) repeat-x;padding-right:5px;}  .fgx1 {border-bottom: 1px solid #4ea9fc;overflow:hidden;margin:10px 0;}  .content1 {width:580px;margin:10px 0;border-bottom: 1px solid #4ea9fc;padding:10px 0px;}  .name {font-weight: bold; line-height:20px;}  .neirong {padding-bottom:15px;}  .list {padding-bottom:15px;}  .list a {font-weight: bold; color:#800080;}  .biaoti {margin-bottom:10px;}  .quxiao {margin:15px 0;text-align:center;}  .photo {padding-top:5px;text-align:center;font-family: Verdana;font-size: 12px; clear: both;line-height:14px; overflow:hidden;}  .photo li {margin-right:16px;float:left;text-align:center;padding:0px;width:126px;height:150px;list-style-type: none; font: 11px Arial;color: #999;}  .photo a:link,a:visited {color:#003399;padding:0px; font: 12px Arial;}  .photo a:hover{color:#FF6600}  .photo_text{float:left;width:103px;}  .ImgBorder{border: 1px solid #ccc; padding:2px;margin-bottom:5px;}  #footer {width: 600px; border-top:2px #4ea9fc solid;text-align:center;margin:5px auto;padding:5px;}  .forget_top {width:572px;height:29px;margin:0 auto;padding:10px 0px 0 10px;overflow:hidden;background: url(http://cn.hisupplier.com/images/forget_top.jpg) no-repeat;font:bold 15px Arial;color: #1757a0;}  .forget_bg {width:568px;margin:0 auto;padding:10px 0px 0 10px; border: 1px solid #d0d0d0;}  .name1 {font-weight: bold; line-height:20px;margin:15px 0;}  .forget_bottom {width: 568px;border: 1px solid #d0d0d0;text-align:center;margin:5px auto;background-color: #f4f4f4;padding:5px;}  .pro_bg {width: 580px;border: 1px solid #d0d0d0;margin:5px auto;background-color: #f4f4f4;padding:5px;}  .contact {padding:10px;margin:0px;clear: both;width: 580px; overflow:hidden;}  .img_box100x100{float:left;width:100px;height:100px;border:1px solid #ccc;display:table-cell;vertical-align:middle;text-align:center;font-size:12px;	margin-bottom: 4px;	background-color: #FFFFFF;	padding-top: 1px;	padding-right: 1px;	padding-bottom: 1px;padding-left: 1px;}  .img_box100x100 img{vertical-align:middle;max-height: 100px;max-width:100px;margin-bottom: 1px;margin-top: expression(( 100 - this.height ) / 2);}  </style>  <script type='text/javascript'>    function setImgWH(theURL, sImage, imgW, imgH) {       var imgObj;      imgObj = new Image();      imgObj.src = theURL;      if ((imgObj.width != 0) && (imgObj.height != 0)) {        if (imgObj.width >= imgW || imgObj.height >= imgH) {          var iHeight = imgObj.height * imgW / imgObj.width;          if (iHeight <= imgH) { );            sImage.width = imgW;            sImage.height = iHeight;          } else {            var iWidth = imgObj.width * imgH / imgObj.height;            sImage.width = iWidth;            sImage.height = imgH;          }        } else {          sImage.width = imgObj.width;          sImage.height = imgObj.height;        }     } else {       sImage.width = imgW;       sImage.height = imgH;  	  }  	}  </script>  </head>  <body>  <div id='web'>    <div id='top2'>询盘信息</div>    <div id='top'><a href='http://cn.hisupplier.com/index.html' target='_blank'><img src='http://cn.hisupplier.com/images/logo.jpg' width='215' height='77' border='0' alt='中国海商网'/></a></div>    <div class='fgx'>2008年11月25日</div>    <div class='name1'>亲爱的张黎明，<br/></div>    <div class='neirong'>      此询盘信息来自 <a href='http://www.nb-lianda.com'>www.nb-lianda.com</a>      ，由&nbsp;<a href='http://cn.hisupplier.com'>中国海商网</a>&nbsp;提供      <div class='fgx1'></div>      测试邮件，为了保证邮件收发正常，收到请回复，谢谢！<br/><br/>      <div class='fgx1'></div><br/>      </div>      <div class='fgx1'></div>      <div class='contact'>         公司名称：浙江海商网络科技有限公司(原海博科技）<br/>        联络人姓名：VICKY<br/>        电子邮件：<a href='mailto:service@haibo.com'>service@haibo.com</a><br/>        电话号码：0574-27718666-1080        <br/>传真号码:0574-27718667        <br/>公司详细地址：宁波市国家级高新区院士路66号创业大厦528 <br/>        网址：<a href='http://http://www.HiSupplier.com'>http://www.HiSupplier.com</a><br/>      </div>    </div>  </div>  <div id='footer'>    <div class='BotTxt'>      <a href='http://cn.hisupplier.com'>首页</a>&nbsp;|&nbsp;       <a href='http://cn.hisupplier.com/product'>供应</a>&nbsp;|&nbsp;      <a href='http://cn.hisupplier.com/buy'>求购</a>&nbsp;|&nbsp;      <a href='http://cn.hisupplier.com/company'>公司</a>&nbsp;|&nbsp;      <a href='http://cn.hisupplier.com/credits'>诚信报告</a>&nbsp;|&nbsp;      <a href='http://cn.hisupplier.com/friendLink'>友情链接</a>&nbsp;|&nbsp;      <a href='http://about.hisupplier.com'>关于海商</a>&nbsp;|&nbsp;      <a href='http://www.hisupplier.com'>国际站</a>&nbsp;|&nbsp;      版权所有&copy;&nbsp;<a title='中国海商网' href='http://cn.hisupplier.com'>中国海商网</a>    </div>  </div>  </body>  </html> ");
		inquiryReply.setSubject("asd");
		inquiryReply.setContent("询盘回复内容");
		inquiryReply.setFilePath("www.s.com");
		Mail mail = MailFactory.getInquiryRely(inquiryReply);
		MailTemplate.writeHtml("E:/MailTemplate", "inquiry_reply.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}

	public void testGetInquiry() throws Exception {
		Inquiry inquiry = new Inquiry();
		inquiry.setToEmail("247018028@qq.com");
		inquiry.setToName("rd20");
		inquiry.setFromEmail("247018028@qq.com");
		inquiry.setSubject("about the <br/> inquiry test!");
		inquiry.setFromCompany("haishang ningbo CO.");
		inquiry.setFromName("alizs");
		inquiry.setFromTel("1203455334");
		inquiry.setFromFax("88745742");
		inquiry.setFromStreet("鄞州鄞县大道123去23饿");
		inquiry.setFromWebsite("www.hisupplier.com");
		inquiry.setContent("询盘的内容！！！！！！\t\r \t\r询盘的内容！！！！！！<br/> <br/> 询盘的内容！！！！！！询盘的内容！！！！！！询盘的内容！！！！！！");
		inquiry.setFilePath("dsfdsfdsfsdf,sdfdsf,dsf");

		BasketItem basketItem = new BasketItem();
		basketItem.setMemberId("guiyou");

		List<BasketItem.Product> productList = new ArrayList<BasketItem.Product>();
		BasketItem.Product product = new BasketItem.Product();
		product.setProName("轮胎");
		product.setProUrl("www.luntan.hisupplier.com");
		product.setImgPath("/20091118/sdfs");
		product.setModel("HOS234");
		productList.add(product);
		product.setProName("轮胎");
		product.setProUrl("www.luntan.hisupplier.com");
		product.setImgPath("/20091118/sdfs");
		product.setModel("HOS234");
		productList.add(product);
		product.setProName("轮胎");
		product.setProUrl("www.luntan.hisupplier.com");
		product.setImgPath("/20091118/sdfs");
		product.setModel("HOS234");
		productList.add(product);
		BasketItem.Product product1 = new BasketItem.Product();
		product1.setProName("轮胎1");
		product1.setProUrl("www.luntan1.hisupplier.com");
		product1.setImgPath("/200911181/sdfs");
		product1.setModel("HOS23411111");
		productList.add(product1);
		product1.setProName("轮胎1");
		product1.setProUrl("www.luntan1.hisupplier.com");
		product1.setImgPath("/200911181/sdfs");
		product1.setModel("HOS23411111");
		productList.add(product1);
		product1.setProName("轮胎1");
		product1.setProUrl("www.luntan1.hisupplier.com");
		product1.setImgPath("/200911181/sdfs");
		product1.setModel("HOS23411111");
		productList.add(product1);
		BasketItem.Product product2 = new BasketItem.Product();
		product2.setProName("轮胎2");
		product2.setProUrl("www.luntan2.hisupplier.com");
		product2.setImgPath("/200911182/sdfs");
		product2.setModel("HOS23422222");
		productList.add(product2);
		basketItem.setProductList(productList);

		List<BasketItem.Trade> tradeList = new ArrayList<BasketItem.Trade>();
		BasketItem.Trade trade = new BasketItem.Trade();
		trade.setTradeName("轮胎");
		trade.setProUrl("www.luntan.hisupplier.com");
		trade.setImgPath("/20091118/sdfs");
		tradeList.add(trade);
		BasketItem.Trade trade1 = new BasketItem.Trade();
		trade1.setTradeName("轮胎1");
		trade1.setProUrl("www.luntan1.hisupplier.com");
		trade1.setImgPath("/200911181/sdfs");
		tradeList.add(trade1);
		BasketItem.Trade trade2 = new BasketItem.Trade();
		trade2.setTradeName("轮胎2");
		trade2.setProUrl("www.luntan2.hisupplier.com");
		trade2.setImgPath("/200911182/sdfs");
		tradeList.add(trade2);
		basketItem.setTradeList(tradeList);

		Mail mail = MailFactory.getInquiry(inquiry, basketItem);
		MailTemplate.writeHtml("E:/MailTemplate", "inquiry.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		sender.run();
//		TaskExecutor.execute(sender);
	}

	public void testGetInquiryRecommend() throws Exception {
		Inquiry inquiry = new Inquiry();
		inquiry.setToEmail("yaozhan189@163.com");
		inquiry.setToName("chenzhong");
		inquiry.setFromEmail("yaozhan189@163.com");
		inquiry.setSubject("about the inquiry test!");
		inquiry.setFromCompany("haishang ningbo CO.");
		inquiry.setFromName("alizs");
		inquiry.setFromTel("1203455334");
		inquiry.setFromFax("88745742");
		inquiry.setFromStreet("鄞州鄞县大道123去23饿");
		inquiry.setFromWebsite("www.hisupplier.com");

		User user = new User();
		user.setMemberId("guiyou");
		user.setCatIds("41");

		Mail mail = MailFactory.getInquiryRecommend(inquiry, user);
		MailTemplate.writeHtml("E:/MailTemplate", "inquiry_recommend.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	
	public void testGetUpgradeMail() throws Exception {
		UpgradeMail upgradeMail = new UpgradeMail();
		upgradeMail.setComName("贵友");
		upgradeMail.setContact("贵友");
		upgradeMail.setEmail("yaozhan189@163.com");
		upgradeMail.setFax("146568565");
		upgradeMail.setMemberId("公寓贵友");
		upgradeMail.setMobile("1345698");
		upgradeMail.setRemark("士大夫诗圣杜甫手动");
		upgradeMail.setTel("46464646");
		upgradeMail.setUpType(1);
		upgradeMail.setUpTypeName("sdfdsfsd");

		Mail mail = MailFactory.getUpgrade(upgradeMail);
		MailTemplate.writeHtml("E:/MailTemplate", "upgrade.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	
	public void testGetAdOrder() throws Exception {
		AdOrder adOrder = new AdOrder();
		adOrder.setComName("贵友");
		adOrder.setContact("贵友");
		adOrder.setEmail("yaozhan189@163.com");
		adOrder.setFax("146568565");
		adOrder.setMemberId("公寓贵友");
		adOrder.setMobile("1345698");
		adOrder.setRemark("士大夫诗圣杜甫手动");
		adOrder.setTel("46464646");
		adOrder.setCatId(41);
		adOrder.setCatName("农业");
		adOrder.setKeyword("士大夫第三方");

		Mail mail = MailFactory.getAdOrder(adOrder);
		MailTemplate.writeHtml("E:/MailTemplate", "adOrder.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
	
	public void testGetTopOrder() throws Exception {
		TopOrder topOrder = new TopOrder();
		topOrder.setComName("贵友");
		topOrder.setContact("贵友");
		topOrder.setEmail("yaozhan189@163.com");
		topOrder.setFax("146568565");
		topOrder.setMemberId("公寓贵友");
		topOrder.setMobile("1345698");
		topOrder.setRemark("士大夫诗圣杜甫手动");
		topOrder.setTel("46464646");
		topOrder.setTopType(1);
		topOrder.setKeyword("关键词啊 啊 啊！");

		Mail mail = MailFactory.getTopOrder(topOrder);
		MailTemplate.writeHtml("E:/MailTemplate", "topOrder.ftl", mail.getContent());

		MailSender sender = MailSenderCNFactory.createSender(MailSenderCNFactory.MY_ACCOUNT);
		sender.add(mail);
		TaskExecutor.execute(sender);
	}
}
