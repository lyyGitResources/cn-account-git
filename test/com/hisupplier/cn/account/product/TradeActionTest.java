/* 
 * Created by baozhimin at Nov 23, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.product.QueryParams;
import com.hisupplier.cn.account.product.TradeAction;
import com.hisupplier.cn.account.test.ActionTestSupport;

/**
 * @author baozhimin
 */
public class TradeActionTest extends ActionTestSupport {

	private String namespace = "/trade";
	private QueryParams params = null;
	private TradeAction action = null;

	public void test_trade_list() throws Exception {
		String method = "trade_list";

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");

		// ���ؼ��ʲ�ѯ
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setQueryText("��");
		this.execute(method, "success");

		// �����ʺŲ�ѯ
		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setUserId(user.getUserId());
		this.execute(method, "success");

		// ��״̬��ѯ
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setState(20);
		this.execute(method, "success");

		// �����������ѯ
		Group group = this.getProductGroup(true, false);
		if (group == null) {
			assertTrue(false);
			return;
		}
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setGroupId(group.getGroupId());
		this.execute(method, "success");

		// ������ID��ѯ
		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_trade_repost() throws Exception {
		String method = "trade_repost";
		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_trade_change_user() throws Exception {
		String method = "trade_change_user";
		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}

		User user = this.getUserEdit();
		if (user == null) {
			assertTrue(false);
			return;
		}

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		params.setUserId(user.getUserId());
		this.execute(method, "success");
	}

	public void test_trade_delete() throws Exception {
		String method = "trade_delete";

		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_trade_add() throws Exception {
		String method = "trade_add";
		//��������
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { 0 });
		this.execute(method, "success");

		//����ͬ������
		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}

		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { proId[0] });
		this.execute(method, "success");

	}

	public void test_trade_edit() throws Exception {
		String method = "trade_edit";

		int[] proId = this.getTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}

		//���м�¼��ѯ��Ĭ�ϣ�
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(new int[] { proId[0] });
		this.execute(method, "success");
	}

	public void test_trade_recycle_list() throws Exception {
		String method = "trade_recycle_list";

		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}

	public void test_trade_recycle_delete() throws Exception {
		String method = "trade_recycle_delete";
		int[] proId = this.getRecycleTradeId();
		if (proId == null) {
			assertTrue(false);
			return;
		}
		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		params.setProId(proId);
		this.execute(method, "success");
	}

	public void test_trade_recycle_empty() throws Exception {
		String method = "trade_recycle_empty";

		action = createAction(TradeAction.class, namespace, method);
		params = action.getModel();
		this.execute(method, "success");
	}
}
