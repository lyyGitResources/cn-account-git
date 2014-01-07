package com.hisupplier.cn.account.webservice;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.hisupplier.cn.account.entity.Inquiry;
import com.hisupplier.cn.account.webservice.InquirySendService;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.basket.BasketItem;
import com.hisupplier.commons.util.DateUtil;

public class ClientTest extends TestCase {

	public void test_inquiry_send() throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/hisupplier/cn/account/webservice/client-beans.xml");

		InquirySendService inquirySendService = (InquirySendService) context.getBean("inquirySendService");

		BasketItem basketItem = new BasketItem();
		//ʹ��guiyou�ʺŲ���
		basketItem.setComId(442);
		basketItem.setMemberId("guiyou");
		basketItem.setMemberType(CN.FREE_SITE);
		basketItem.setEmail("linliuwei@vip.qq.com");
		basketItem.setContact("guiyou");
		basketItem.setUserId(758);

		BasketItem.Product product = new BasketItem.Product();
		product.setProName("��̥");
		product.setProUrl("www.luntan.hisupplier.com");
		product.setImgPath("/20091118/sdfs");
		product.setModel("HOS234");

		BasketItem.Trade trade = new BasketItem.Trade();
		trade.setTradeName("��̥");
		trade.setProUrl("www.luntan.hisupplier.com");
		trade.setImgPath("/20091118/sdfs");

		List<BasketItem.Product> productList = new ArrayList<BasketItem.Product>();
		//		productList.add(product);
		//		productList.add(product);
		//		productList.add(product);
		//		productList.add(product);
		//		productList.add(product);

		List<BasketItem.Trade> tradeList = new ArrayList<BasketItem.Trade>();
		tradeList.add(trade);
		tradeList.add(trade);
		tradeList.add(trade);
		tradeList.add(trade);
		tradeList.add(trade);

		basketItem.setProductList(productList);
		basketItem.setTradeList(tradeList);

		List<BasketItem> basketItemList = new ArrayList<BasketItem>();
		basketItemList.add(basketItem);

		Inquiry inquiry = new Inquiry();
		inquiry.setComId(442);
		inquiry.setSubject("fafffff");
		inquiry.setContent("cccccccc");
		inquiry.setFromIP("192.168.1.1");
		inquiry.setFromSite(1); //������վ
		inquiry.setBasketItemList(basketItemList);
		inquiry.setLoginUser(null);

		//���û�
		inquiry.setNewUser(true);
		inquiry.setPasswd("123456");
		inquiry.setSubject("ѯ������");
		inquiry.setContent("������sdf");
		inquiry.setSex(1);
		inquiry.setFromEmail(this.getRandom() + "@163.com");
		inquiry.setFromCompany(this.getRandom());
		inquiry.setFromName("����");
		inquiry.setFromProvince("103103");//�㽭��������۴��
		inquiry.setFromCity("103103102");
		inquiry.setFromTown("103103102106");
		inquiry.setFromTel("0574-27886899");
		inquiry.setFromFax("0574-27886899");
		inquiry.setFromStreet("۴�ش��1357��");
		inquiry.setFromWebsite("www.hisupplier.com");
		inquiry.setFromPage("www.hisupplier.com");
		inquiry.setToEmail("");
		inquiry.setToName("");
		//�ǻ�Ա
		//		inquiry.setNewUser(false);
		//		inquiry.setEmail("guiyou");
		//		inquiry.setPasswd("haibo2.2");
		inquirySendService.send(inquiry);
	}

	public void test_join() throws Exception {
		@SuppressWarnings("unused")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/hisupplier/cn/account/webservice/client-beans.xml");
	}

	public void test_subscibe() throws Exception {
		@SuppressWarnings("unused")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("com/hisupplier/cn/account/webservice/client-beans.xml");
	}

	private String getRandom() {
		DateUtil dateUtil = new DateUtil();
		return dateUtil.getDate2() + dateUtil.getTime2();
	}

}
