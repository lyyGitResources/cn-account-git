/* 
 * Created by baozhimin at Nov 16, 2009 
 * Copyright HiSupplier.com 
 */

package com.hisupplier.cn.account.product;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.util.CellRangeAddressList;

import com.hisupplier.cn.account.basic.LoginUser;
import com.hisupplier.cn.account.dao.UpdateMap;
import com.hisupplier.cn.account.entity.Company;
import com.hisupplier.cn.account.entity.Group;
import com.hisupplier.cn.account.entity.Image;
import com.hisupplier.cn.account.entity.Product;
import com.hisupplier.cn.account.entity.User;
import com.hisupplier.cn.account.entity.UserLog;
import com.hisupplier.cn.account.entity.Video;
import com.hisupplier.cn.account.group.GroupDAO;
import com.hisupplier.cn.account.group.SpecialGroupDAO;
import com.hisupplier.cn.account.member.CompanyDAO;
import com.hisupplier.cn.account.member.UserDAO;
import com.hisupplier.cn.account.misc.VideoDAO;
import com.hisupplier.cn.account.util.GroupUtil;
import com.hisupplier.cn.account.util.ImageAlt;
import com.hisupplier.cn.account.util.UserLogUtil;
import com.hisupplier.cn.account.view.Button;
import com.hisupplier.commons.CN;
import com.hisupplier.commons.entity.Attachment;
import com.hisupplier.commons.entity.Category;
import com.hisupplier.commons.entity.cn.Tag;
import com.hisupplier.commons.entity.cn.TagValue;
import com.hisupplier.commons.exception.PageNotFoundException;
import com.hisupplier.commons.page.ListResult;
import com.hisupplier.commons.util.CategoryUtil;
import com.hisupplier.commons.util.Coder;
import com.hisupplier.commons.util.DateUtil;
import com.hisupplier.commons.util.FileUtil;
import com.hisupplier.commons.util.StringUtil;
import com.hisupplier.commons.util.TextUtil;
import com.hisupplier.commons.util.UploadUtil;

/**
 * @author baozhimin
 */
public class ProductService {

	private static Pattern number_pattern = Pattern.compile("^\\d{1}$");
	public static int RECYCLE_MAX = 200;
	private final int ROW_RECORD =100;
	private final int BATCH_COUNT_MAX = 20;
	private CompanyDAO companyDAO;
	private UserDAO userDAO;
	private ProductDAO productDAO;
	private NewProductDAO newProductDAO;
	private GroupDAO groupDAO;
	private SpecialGroupDAO specialGroupDAO;
	private TradeDAO tradeDAO;
	private VideoDAO videoDAO;
	
	/**
	 * ����excel���
	 * @param product
	 * @param path
	 * @return
	 */
	public String createExcelFile(Product product, String fileDir){
		String fileName ="hisupplier.xls";
		if(product.getCatId() > 0){
			fileName = "hisupplier_cn_" + product.getCatId() + ".xls";
		}else{
			return "error";
		}
		try {
			File file = new File(fileDir + "/" + fileName);
			if(file.exists()){
				FileUtil.delete(fileDir + "/" + fileName);
			}else{
				file = FileUtil.createNewFile(fileDir, fileName);
			}
			//���ļ�
			WritableWorkbook book = Workbook.createWorkbook(file);
			WritableSheet firstSheet = book.createSheet("������Ʒ��Ϣ�༭", 0);
			WritableSheet secondSheet = book.createSheet("�༭˵��", 1);
			processFirstSheet(firstSheet, product);
			processSecondSheet(secondSheet);
			//д�����ݲ��ر��ļ�
			book.write();
			book.close();
			processFirstSheetForMinOrderUnit(fileDir + "/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	/**
	 * ������Ӳ�Ʒ
	 * @param response
	 * @param product
	 * @param loginUser
	 * @return
	 */
	public Map<String,Object> batchAddProduct(HttpServletRequest request, 
			HttpServletResponse response, Product product, 
			LoginUser loginUser, String fileDir) {
	
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("successCount", 0);
		result.put("errorCount", 0);
		result.put("sizeLimit", 0);
		result.put("error", "");
		List<Product> productList = new ArrayList<Product>();
		//���������к���ֵ����ʽ��(5-7-1.��Ʒ�Ĵ�����ʾ��Ϣ),����6�е�8��
		List<String> errorList = new ArrayList<String>();
		//������ȷ����ֵ
		List<Integer> successRowList = new ArrayList<Integer>();
		Workbook workBook = null;
		int rowCount = 0;
		int columnCount = 0;
		int realRowData = 0;//������ж�������ʵ����
		Product pro = null;
		String catId = "";
		// ��ȡ��˾��Ϣ�ͻ�Ա�ȼ�������
		Company company = this.companyDAO.findCompanyMemberType(loginUser.getComId());
		try {
			workBook =  Workbook.getWorkbook(product.getAttachment());
			Sheet sheet = workBook.getSheet(0);
			if(sheet == null){
				result.put("error", "��ѡ����ȷ���ļ�");
				return result;
			}
			if(sheet.getColumns() <=0 || sheet.getRows() <= 0){
				result.put("error", "��ѡ����ȷ���ļ�");
				return result;
			}
			Cell cell= sheet.getCell(0, 0);
			if(cell == null){
				result.put("error", "��ѡ����ȷ���ļ�");
				return result;
			}
			catId = cell.getContents();//���catId
			if(StringUtil.isEmpty(catId)){
				result.put("error", "����ѡ�����������ѡ�����");
				return result;
			}
			rowCount = sheet.getRows();
			List<Tag> tagList = this.parseTags(Integer.parseInt(catId), null);
			columnCount = 9+tagList.size();
			
			String proName = "";
			String keyword1 = "";
			String imgName = "";
			String brief = "";
			String tagName1 = "";
			String tagName2 = "";
			String tagValue1 = "";
			String tagValue2 = "";
			int optimizeCount = 0;//�Ż���Ʒ������
			
			String field = "";
			String fieldValue = "";
			String imgInfo = sheet.getCell(2, 0).getContents();//���ͼƬ��Ϣ
			Map<String,String> imgMap = new HashMap<String,String>();
			if(StringUtil.isNotEmpty(imgInfo)){
				String[] tmp = imgInfo.split(",");
				String tmpName = "";
				String tmpPath = "";
				for(String singleImgInfo : tmp){
					tmpName = singleImgInfo.split(";")[0].split(":")[1];
					tmpPath = singleImgInfo.split(";")[1].split(":")[1];
					imgMap.put(Coder.decodeURL(tmpName.replaceAll("%", "")), Coder.decodeURL(tmpPath));
				}
			}

			//ѭ����ÿһ����Ʒ��Ϣ���������ݿ�
			for(int i=4;i<rowCount;i++){//��
				pro = new Product();
				
				proName = "";
				keyword1 = "";
				imgName = "";
				brief = "";
				tagName1 = "";
				tagName2 = "";
				tagValue1 = "";
				tagValue2 = "";
				boolean hasRowData = false;//�����Ƿ�������
				boolean error = false;//�Ƿ��д�����У�����оͲ���ӵ���Ʒ�б�������ݿ�������
				boolean isOptimize = false;//�Ƿ����Ż���Ʒ
				for(int j=0;j<columnCount;j++){
					field = sheet.getCell(j,2).getContents();
					fieldValue = sheet.getCell(j,i).getContents();
					//��Ʒ����
					if("proName".equals(field)){
						proName = fieldValue;
						if(StringUtil.isNotEmpty(proName)){
							hasRowData = true;
							if(proName.length() > 120){
								errorList.add(i+"-"+j+"-"+(j+1)+".��Ʒ����120���ַ��ڡ�");
								error = true;
							}else{
								pro.setProName(proName);
							}
						}
					}else if("keyword1".equals(field)){
						keyword1 = fieldValue;
						if(!"".equals(keyword1)){
							hasRowData = true;
							isOptimize = true;
						}
						pro.setKeyword1(keyword1);
					}else if("imgPath".equals(field)){
						imgName = fieldValue;
						if(StringUtil.isNotEmpty(imgName) && imgMap.size() > 0){
							String tmppath = imgMap.get(imgName);
							if(tmppath != null){
								pro.setImgPath(Coder.encodeURL(tmppath));
								hasRowData = true;
							}
						}
					}else if("brief".equals(field)){
						brief = fieldValue;
						if(StringUtil.isNotEmpty(brief)){
							hasRowData = true;
							if (brief.length() > 150) {
								errorList.add(i + "-" + j + "-" + (j + 1) + ".��ƷժҪ150���ַ��ڡ�");
								error = true;
							} else {
								pro.setBrief(brief);
							}
						}
					}else if("description".equals(field)){
						if(!"".equals(fieldValue)){
							hasRowData = true;
							pro.setDescription(fieldValue);
						}
					}else if(field.indexOf("tagName1") != -1){
						if(StringUtil.isNotEmpty(fieldValue)){
							hasRowData = true;
							tagName1 += "," + field.split("_")[2];//�õ���ǩ��attr_tagName1_2563��
							String tmp = sheet.getCell(j,0).getContents();//�õ���ǩֵ�ԣ���,34757;è,34758;����,34759��
							Map<String,String> map = new HashMap<String,String>();
							for(String label : tmp.split(";")){
								map.put(label.split(",")[0], label.split(",")[1]);
							}
							tagValue1 += "," + map.get(fieldValue);
						}
					}else if(field.indexOf("tagName2") != -1){
						if(StringUtil.isNotEmpty(fieldValue)){
							hasRowData = true;
							if(fieldValue.length() > 120 && hasRowData){
								errorList.add(i+"-"+j+"-"+(j+1)+".��ǩֵ120���ַ���");
								error = true;
							}
							tagName2 +="," + field.split("_")[2];
							tagValue2 += "," + fieldValue;
						}
					}else if("minOrderNum".equals(field)){
						if(!"".equals(fieldValue)){
							hasRowData = true;
							if(!Pattern.compile("^\\d+$").matcher(fieldValue).matches()){
								errorList.add(i+"-"+j+"-"+(j+1)+".��С����Ϊ������С��99999����������");
								error = true;
							}else{
								pro.setMinOrderNum(Integer.parseInt(fieldValue));
							}
						}
					}else if("minOrderUnit".equals(field)){
						if(!"".equals(sheet.getCell(columnCount-4,i).getContents()) && "".equals(fieldValue)){
							errorList.add(i+"-"+j+"-"+(j+1)+".������λ����Ϊ�ա�");
							error = true;
						} else if(!"".equals(fieldValue)){
							hasRowData = true;
							pro.setMinOrderUnit(fieldValue);
						}
					}else if("price".equals(field)){
						if(!"".equals(fieldValue)){
							hasRowData = true;

								String[] priceArray = fieldValue.split("~");
								if(priceArray.length > 1){
									if(!Pattern.compile("^\\d{1,8}(\\.\\d{0,2})?$").matcher(priceArray[0]).matches()){
										errorList.add(i+"-"+j+"-"+(j+1)+".�۸�����������������8λ֮�ڣ�С�����ֿ�����2λ֮��");
										error = true;
									}else{
										pro.setPrice1(Float.parseFloat(priceArray[0]));
									}
									if(!Pattern.compile("^\\d{1,8}(\\.\\d{0,2})?$").matcher(priceArray[1]).matches()){
										errorList.add(i+"-"+j+"-"+(j+1)+".�۸�����������������8λ֮�ڣ�С�����ֿ�����2λ֮��");
										error = true;
									}else{
										pro.setPrice2(Float.parseFloat(priceArray[1]));
									}
								}else{
									if(!Pattern.compile("^\\d{1,8}(\\.\\d{0,2})?$").matcher(fieldValue).matches()){
										errorList.add(i+"-"+j+"-"+(j+1)+".�۸�����������������8λ֮�ڣ�С�����ֿ�����2λ֮��");
										error = true;
									}else{
										pro.setPrice1(Float.parseFloat(fieldValue));
									}
								}
						}
					}else if("transportation".equals(field)){
						if(!"".equals(fieldValue)){
							hasRowData = true;
							pro.setTransportation(fieldValue);
						}
					}
				}
				if("".equals(brief) || "".equals(pro.getBrief())){
					if(hasRowData){
						errorList.add(i+"-3"+"-"+"4.��ƷժҪ������150���ַ��ڡ�");
					}
					error = true;
				}
				if("".equals(proName) || "".equals(pro.getProName())){
					if(hasRowData){
						errorList.add(i+"-0"+"-"+"1.��Ʒ����120���ַ��ڡ�");
					}
					error = true;
				}
				if(hasRowData && isOptimize){
					optimizeCount++;
				}
				if(company.getOptimizeProCount() + optimizeCount > company.getOptimizeProMax()){
					errorList.add(i+"-"+0+"-�Ż���Ʒ�ѳ����Ż���Ʒ����ϴ�����");
					break;
				}
				if(company.getProductCount()+productList.size() >= company.getProductMax() && hasRowData){
					errorList.add(i+"-"+0+"-�ѳ�����Ʒ����ϴ�����");
					break;
				}
				QueryParams tmpParam = new QueryParams();
				tmpParam.setKeyword1(keyword1);
				tmpParam.setLoginUser(loginUser);
				if(!"true".equals(checkProductKeywordForBacth(tmpParam))){
					errorList.add(i+"-1-4.�ؼ����Ѿ����ڡ�");
					error = true;
				}
				if(!error){
					successRowList.add(i);
					if(tagName1.length() > 0){
						pro.setTagName1(StringUtil.toArray(tagName1.substring(1,tagName1.length()),","));
					}
					if(tagValue1.length() > 0){
						pro.setTagValue1(StringUtil.toArray(tagValue1.substring(1,tagValue1.length()),","));
					}
					if(tagName2.length() > 0){
						pro.setTagName2(StringUtil.toArray(tagName2.substring(1,tagName2.length()),","));
					}
					if(tagValue2.length() > 0){
						pro.setTagValue2(StringUtil.toArray(tagValue2.substring(1,tagValue2.length()),","));
					}
					pro.setUserId(loginUser.getUserId());
					pro.setCatId(Integer.parseInt(catId));
					pro.setCatName(sheet.getCell(0, 1).getContents());
					pro.setComId(loginUser.getComId());
					pro.setProvince("");
					pro.setCity("");
					pro.setTown("");
					Image img = new Image();
					img.getWatermark(request, loginUser.getMemberId());
					pro.setImage(img);
					pro.setModel("");
					pro.setPlace("");
					pro.setPaymentType("");
					pro.setProductivity("");
					pro.setPacking("");
					pro.setDeliveryDate("");
					pro.setFilePath("");
					if(pro.getMinOrderUnit() == null){
						pro.setMinOrderUnit("");
					}
					if(pro.getTransportation() == null){
						pro.setTransportation("");
					}
					if(pro.getDescription() == null){
						pro.setDescription("");
					}
					if(pro.getImgPath() == null){
						pro.setImgPath("");
					}
					productList.add(pro);
				}
				if(hasRowData){
					realRowData++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("product", product);
		result.put("errorFilePaht", "/productexcel/temp/" + loginUser.getMemberId() + 
				"/" +"hisupplier_cn_" + catId + "_error.xls");
		result.put("successCount", productList.size());
		result.put("errorCount", errorList.size());
		if(productList.size() > BATCH_COUNT_MAX){//���ֻ���������ϴ�20����Ʒ
			result.put("sizeLimit", BATCH_COUNT_MAX);
			return result;
		}
		if(productList.size() <= 0 && company.getProductCount() >= company.getProductMax()){
			result.put("error", "���Ĳ�Ʒ�����Ѵﵽ"+company.getProductCount()+"�������������");
		}else if(productList.size() <= 0 && realRowData <=0){
			result.put("error", "����д��Ʒ��Ϣ");
		}
		for(Product tmp : productList){
			tmp.setFoodPact(product.isFoodPact());
			addProduct(response, tmp, loginUser);
		}
		if(errorList.size() > 0){//����һ��excel��д��������
			WritableWorkbook copy = null;
			WritableSheet firstSheet = null;
			try {
				File errorFile = new File(fileDir, "hisupplier_cn_" + catId + "_error.xls");
				if(errorFile.exists()){
					FileUtil.delete(fileDir + "/hisupplier_cn_" + catId + "_error.xls");
				}
				errorFile = FileUtil.createNewFile(fileDir, "hisupplier_cn_" + catId + "_error.xls");
				copy = Workbook.createWorkbook(errorFile, workBook);
				firstSheet = copy.getSheet(0);
				
				WritableCellFormat wcf_white = new WritableCellFormat();
				wcf_white.setBackground(Colour.WHITE);
				wcf_white.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
				
				for(int row=4;row<firstSheet.getRows();row++){
					firstSheet.addCell(new Blank(columnCount,row));//��ʼ��������ʾ�У������һ��
					for(int column=0;column < columnCount;column++){//ȥ�������еĵ�ɫ
						firstSheet.getWritableCell(column,row).setCellFormat(wcf_white);
					}
				}
				WritableCellFormat wcf_error = new WritableCellFormat();
				wcf_error.setBackground(Colour.RED);
				wcf_error.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
				
				WritableCell cell = null;
				String errorTip = "";//������ʾ��
				String tmp = "";//�������ݵ�����ֵ����ʾ��磺6-0-1.��Ʒ����120���ַ��ڡ�
				Label tipLabel = null;

				for(int i=(errorList.size()-1);i>=0;i--){
					tmp = errorList.get(i);
					cell = firstSheet.getWritableCell(Integer.parseInt(tmp.split("-")[1]),Integer.parseInt(tmp.split("-")[0]));
					cell.setCellFormat(wcf_error);
					errorTip = firstSheet.getWritableCell(columnCount,Integer.parseInt(tmp.split("-")[0])).getContents();
					errorTip += tmp.split("-")[2];
					tipLabel = new Label(columnCount, Integer.parseInt(tmp.split("-")[0]), errorTip ,wcf_error);
					firstSheet.addCell(tipLabel);
					firstSheet.setColumnView(columnCount, 200);//������ʾ��Ԫ����
				}
				for(int i = 0;i<successRowList.size();i++){
					firstSheet.removeRow(successRowList.get(i) - i);
				}
				copy.write();
				copy.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * ��ͨ��Ʒ����
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 * 		model
	 * 		keywords
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 *  company
	 *  userList
	 * </pre>
	 */
	public Map<String, Object> getProductList(QueryParams params) {
		Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		// ����������δͨ����Ʒ�����ǵ�һ�ν����Ʒ�б�ҳ�棬���ÿ������Ϊ���δͨ��
		if (params.isShowReject() && company.getProductRejectCount() > 0 && params.getLoginUser().isAdmin()) {
			params.setState(CN.STATE_REJECT);
		}

		Map<String, Object> result = new HashMap<String, Object>();
		ListResult<Product> listResult = this.productDAO.findProductList(params);
		result.put("listResult", listResult);

		Map<Integer, Integer> copyProIdMap = this.tradeDAO.findTradeCopyProId(params.getLoginUser().getComId());
		for (Product product : listResult.getList()) {
			if (params.getLoginUser().isAdmin() || params.getLoginUser().getUserId() == product.getUserId()) {
				product.addOperate(new Button("/product/product_edit.htm").appendParam("proId", product.getProId()).setName("button.editProduct").getLink());
				product.addOperate("<br />");
			}

			// �ж��ܷ�תΪ����
			if (!copyProIdMap.containsKey(product.getProId())) {
				product.addOperate(new Button("/trade/trade_add.htm").appendParam("copyProId", product.getProId()).setName("button.toTrade").getLink());
				product.addOperate("<br />");
			} else {
				product.addOperate("<a onclick='isModifyTrade(" + copyProIdMap.get(product.getProId()) + ")' href='javascript:void(0)'>" + TextUtil.getText("button.toTrade", "zh") + "</a>");
				product.addOperate("<br />");
			}
			product.addOperate(new Button("/product/product_add.htm").appendParam("proId", product.getProId()).setName("button.addSameProduct").getLink());
			product.addOperate("<br />");
			if (product.isShowNewIco()) {
				product.addOperate("<span id=\"productNewIco" + product.getProId() + "\"><a href='javascript:showNewIco(" + product.getProId() + ",false)'>�²�Ʒ</a></span>");
				product.addOperate("<br />");	
			} else {
				product.addOperate("<span id=\"productNewIco" + product.getProId() + "\"><a href='javascript:showNewIco(" + product.getProId() + ",true)'>�²�Ʒ</a></span>");
				product.addOperate("<br />");
			}
		}

		if (params.isAjax()) {
			return result;
		}

		// ���ƻ�Ա���Ҵ������ʺ�
		if (company.getMemberType() == CN.GOLD_SITE && company.getUserCount() > 0) {
			result.put("userList", this.userDAO.findUserList(params.getLoginUser().getComId()));
		}
		result.put("company", company);
		result.put("generalProCount", company.getProductCount() - company.getOptimizeProCount());
		return result;
	}
	/**
	 * ͨ��groupId��ѯ���ڲ�Ʒ��Ϣ
	 * @param params
	 * @return
	 */
	public Map<String, Object> getProductByGroupId(QueryParams params){
		Map<String, Object> result = new HashMap<String, Object>();
		params.setSortBy("groupOrder");
		params.setSortOrder("asc");
		params.setState(-2);
		result.put("listResult", this.productDAO.findProductList(params));
		return result;
	}

	/**
	 * ��ͨ��Ʒ����վ�б�
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 * 		model
	 * 		keywords
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 *  recycleMax
	 *  productDelCount
	 * </pre>
	 */
	public Map<String, Object> getProductRecycleList(QueryParams params) {
		Company company = this.companyDAO.findCompany(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}

		Map<String, Object> result = new HashMap<String, Object>();
		params.setState(CN.STATE_RECYCLE);

		ListResult<Product> listResult = this.productDAO.findProductList(params);

		for (Product product : listResult.getList()) {
			product.addOperate(new Button("/product/product_edit.htm").appendParam("proId", product.getProId()).setName("button.restore").getLink());
		}
		result.put("recycleMax", ProductService.RECYCLE_MAX);
		result.put("productDelCount", company.getProductDelCount());
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * ��ͨ��Ʒ�����б�
	 * @param params groupId
	 * @return
	 * <pre>
	 * groupError
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 * 		model
	 * 		keywords
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getProductOrderList(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		// ���ڲ�Ʒ
		if (params.getGroupId() > 0) {
			// �ж�groupId��Ч��
			GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(params.getLoginUser().getComId()));
			Group group = groupUtil.get(params.getGroupId());
			if (group == null ) {
				result.put("groupError", "product.groupError");
				return result;
			}
			params.setGroupIds(StringUtil.toIntArray(StringUtil.toArray(groupUtil.getChildIds(params.getGroupId(), true), ",")));
			params.setSortBy("groupOrder");
		} else {
			params.setSortBy("listOrder");
		}
		params.setSortOrder("asc");
		params.setState(-2);

		result.put("listResult", this.productDAO.findProductList(params));
		return result;
	}

	/**
	 * չ̨��Ʒ�б�
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 * 		model
	 * 		keywords
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 *  featureProMax
	 *  featureProCount
	 * </pre>
	 */
	public Map<String, Object> getFeatureProductList(QueryParams params) {
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		//��ѯ״̬��15��20��չ̨��Ʒ
		params.setState(-2);
		params.setFeature(1);
		params.setSortBy("featureOrder");
		params.setSortOrder("asc");
		params.setPageSize(16);
		ListResult<Product> listResult = this.productDAO.findProductList(params);
		List<Group> groupList = this.groupDAO.findGroupList(params.getLoginUser().getComId());

		for (Product product : listResult.getList()) {
			if (product.getFeatureGroupId() > 0) {
				product.setGroupName(new GroupUtil(groupList).get(product.getFeatureGroupId()).getGroupName());
			}
			//			product.setProName(StringUtil.substring(product.getProName(),4,"..."));
			//			product.setModel(StringUtil.substring(product.getModel(),4,"..."));
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		result.put("featureProMax", company.getFeatureProMax());
		result.put("featureProCount", company.getFeatureProCount());
		return result;
	}

	/**
	 * ��չ̨��Ʒ�б�
	 * @param params
	 * @return
	 * <pre>
	 * listResult
	 *   Product{
	 *      proId
	 * 		userId
	 * 		proName
	 * 		imgId
	 * 		imgPath
	 * 		model
	 * 		keywords
	 *		viewCount
	 * 		state
	 * 		modifyTime
	 *    }
	 * </pre>
	 */
	public Map<String, Object> getNoFeatureProductList(QueryParams params) {
		//��ѯ״̬��15��20�ķ�չ̨��Ʒ
		params.setState(-2);
		params.setFeature(0);

		ListResult<Product> listResult = this.productDAO.findProductList(params);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("listResult", listResult);
		return result;
	}

	/**
	 * ��ͨ��Ʒ���
	 * @param request
	 * @param params
	 * <pre>
	 * 1) loginUser ��¼��Ϣ
	 * 2) proId	��ƷID�����ͬ���Ʒʱ����
	 * 3) newProId	��ƷID�����ܲ�ƷתΪ��ͨ��Ʒʱ����
	 * 4) groupId ��ͨ����ID���ڷ�������Ӳ�Ʒʱ����
	 * </pre>
	 * @return
	 * <pre>
	 * full: ��ʾ��Ʒ�������
	 * num: ��ӵ�����
	 * product����Ʒ��Ϣ
	 * productCount ��Ʒ����
	 * productMax ��Ʒ
	 * </pre>
	 */
	public Map<String, Object> getProductAdd(HttpServletRequest request, 
			QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int userId = params.getLoginUser().getUserId();
		Map<String, Object> result = new HashMap<String, Object>();
		
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		// ��Ʒ����
		if (company.getProductCount() >= company.getProductMax()) {
			result.put("full", "product.full");
			result.put("num", company.getProductCount() + "");
			return result;
		}
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		// ���ͬ���Ʒ
		Product product = new Product();
		if (params.getProId() != null && params.getProId()[0] > 0) {
			product = this.productDAO.findProduct(comId, params.getProId()[0]);
			if(product == null){
				product = new Product();
			}
			product.setProId(0);
			product.setImgId(0);
			product.setKeywords("");
			product.setImgPath("");
			product.setVideoId(0);
			product.setProName("");
			product.setModel("");
			product.setFilePath("");
			product.setDescription("");
			
			// �ֽ��ǩ
			if (StringUtil.isNotBlank(product.getTags())) {
				product.setTagList(parseTags(product.getCatId(), product.getTags()));
			}
			// תΪ��ͨ��Ʒ
		} else if (params.getNewProId() > 0) {
			Product newproduct = newProductDAO.findNewProduct(comId, params.getNewProId());
			product.setProName(newproduct.getProName());
			product.setModel(newproduct.getModel());
			product.setProvince(newproduct.getProvince());
			product.setCity(newproduct.getCity());
			product.setTown(newproduct.getTown());
			product.setPlace(newproduct.getPlace());
			product.setBrief(newproduct.getBrief());
			product.setDescription(newproduct.getDescription());
		}

		// Ĭ�ϲ�Ʒ���ֶ�
		int groupId = params.getGroupId() > 0 ? 
				params.getGroupId() : product.getGroupId();
		if (groupId > 0) {
			product.setGroupName(new GroupUtil(this.groupDAO.findGroupList(comId))
				.getNamePath(groupId, " >> "));
			product.setGroupId(groupId);
		}
		// ��������
		if (company.getTradeCount() >= company.getTradeMax()) {
			product.setAddTrade(false);
		}
		// �Ż���Ʒ����
		if (company.getOptimizeProCount() >= company.getOptimizeProMax()) {
			product.setOptimizeProduct(false);
		}

		// ����Ŀ¼����
		if (product.getCatId() > 0) {
			product.setCatName(CategoryUtil.getNamePath(product.getCatId(), " >> "));
		}

		// Ĭ��ԭ����
		if (StringUtil.isEmpty(product.getTown())) {
			User user = this.userDAO.findUser(userId, comId);
			product.setProvince(user.getProvince());
			product.setCity(user.getCity());
			product.setTown(user.getTown());
		}

		// ����ؼ���
		product = this.parseKeywords(product);

		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("product", product);
		result.put("image", image);
		result.put("productCount", company.getProductCount());
		result.put("productMax", company.getProductMax());
		result.put("company", company);
		return result;
	}
	/**
	 * ��������ϴ�ʱ������
	 * @param request
	 * @param params
	 * @return
	 */
	public Map<String, Object> getProductBatchAdd(HttpServletRequest request, QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		result.put("isImgFull", false);
		result.put("company", company);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		return result;
	}
	/**
	 * ѡ������Ŀ¼
	 * @param params
	 * @return
	 */
	public Map<String, Object> getSimilarCategory(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Integer> catIdList = this.productDAO.findCatIdList(params.getLoginUser().getComId(), 15);
		List<Integer> tradeCatIdList = this.tradeDAO.findCatIdList(params.getLoginUser().getComId(), 15);
		Map<Integer, String> catMap = new HashMap<Integer, String>();
		int catIdSize = catIdList.size();
		int tradeCatIdSize = tradeCatIdList.size();
		for (int i = 0; i < 15; i++) {
			Integer catId = 0;
			String namePath = "";
			if (i < catIdSize && catMap.size() < 15) {
				catId = catIdList.get(i);
				if (StringUtil.isNotEmpty(namePath = CategoryUtil.getNamePath(catId, " >> "))) {
					catMap.put(catId, namePath);
				}
			}
			if (i < tradeCatIdSize && catMap.size() < 15) {
				catId = tradeCatIdList.get(i);
				if (StringUtil.isNotEmpty(namePath = CategoryUtil.getNamePath(catId, " >> "))) {
					catMap.put(catId, namePath);
				}
			}
			if (catMap.size() == 15) break;
		}
		result.put("catMap", catMap);
		return result;
	}

	/**
	 * ��Ʒ�޸�
	 * @param request
	 * @param params
	 * <pre>
	 * 1) loginUser
	 * 2) proId
	 * </pre>
	 * @return
	 * <pre>
	 * product
	 * copyProId	��Ʒ��Ӧ�����ID
	 * image		ͼƬˮӡ��Ϣ
	 * </pre>
	 */
	public Map<String, Object> getProductEdit(HttpServletRequest request, QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();

		if (params.getProId() == null) {
			return null;
		}

		Product product = this.productDAO.findProduct(params.getLoginUser().getComId(), params.getProId()[0]);
		if (product == null) {
			throw new PageNotFoundException();
		}
		Company company = this.companyDAO.findCompanyMemberType(params.getLoginUser().getComId());
		if (company == null) {
			throw new PageNotFoundException();
		}
		
		result.put("isImgFull", false);
		// ͼƬ����
		if (company.getImgCount() >= company.getImgMax()) {
			//ͼƬ����
			result.put("isImgFull", true);
		}
		// ����ؼ���
		if (StringUtil.isEmpty(product.getKeywords())) {
			if (company.getOptimizeProCount() >= company.getOptimizeProMax()) {
				product.setOptimizeProduct(false);
			}
		} 
		product = this.parseKeywords(product);
		// ������ͨ����
		if (product.getState() == CN.STATE_REJECT && product.getGroupIdBak() > 0) {
			product.setGroupId(product.getGroupIdBak());
		}
		if (product.getGroupId() > 0) {
			product.setGroupName(new GroupUtil(this.groupDAO.findGroupList(params.getLoginUser().getComId())).getNamePath(product.getGroupId(), " >> "));
		}

		// �����������
		List<Group> specialGroupList = this.specialGroupDAO.findGroupList(params.getLoginUser().getComId(), params.getProId());
		if (specialGroupList.size() > 0) {
			Map<Integer, String> specialGroupMap = new HashMap<Integer, String>();
			for (int i = 0; i < specialGroupList.size(); i++) {
				specialGroupMap.put(specialGroupList.get(i).getGroupId(), specialGroupList.get(i).getGroupName());
			}
			product.setSpecialGroupMap(specialGroupMap);
		}

		// ����Ŀ¼
		if (product.getCatId() > 0) {
			product.setCatName(CategoryUtil.getNamePath(product.getCatId(), " >> "));
		}

		// �ֽ��ǩ
		if (StringUtil.isNotBlank(product.getTags())) {
			product.setTagList(this.parseTags(product.getCatId(), product.getTags()));
		}

		if (product.getVideoId() > 0) {
			Video video = this.videoDAO.findVideo(params.getLoginUser().getComId(), product.getVideoId());
			product.setVideoImgPath(video.getImgPath());
			product.setPlayPath(video.getPlayPath());
			product.setVideoState(video.getState());
		}

		// �ܷ�ת��Ϊ����
		int copyProId = 0;
		Map<Integer, Integer> copyProIdMap = this.tradeDAO.findTradeCopyProId(params.getLoginUser().getComId());
		if (copyProIdMap.containsKey(params.getProId()[0])) {
			copyProId = copyProIdMap.get(params.getProId()[0]);
		}

		Image image = new Image();
		image.getWatermark(request, company.getMemberId());

		result.put("product", product);
		result.put("copyProId", copyProId);
		result.put("image", image);
		result.put("company", company);
		return result;
	}

	/**
	 * ѡ���ǩ
	 * @param params 
	 * @return
	 */
	public Map<String, Object> getProductTags(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("tagsList", this.parseTags(params.getCatId(), null));
		return result;
	}

	/**
	 * ��֤����ʱ�����������IDȡ�������������
	 * @param product
	 * @return
	 * <pre>
	 * productCount
	 * productMax
	 * product
	 * </pre>
	 */
	public Map<String, Object> getProductSubmitError(Product product, LoginUser user) {
		Map<String, Object> result = new HashMap<String, Object>();
		// ���������Ҫ������
		if (product.getProId() == 0) {
			Company company = null;
			//���product.getComId() == 0 ˵����sessionʧЧ�����µ�½���͹��������󣬴�ʱproduct�����κ����ݣ�������ת�򵽲�Ʒ���ҳ������
			if (product.getComId() == 0) {
				company = this.companyDAO.findCompanyMemberType(user.getComId());
			} else {
				company = this.companyDAO.findCompanyMemberType(product.getComId());
			}
			if (company == null) {
				throw new PageNotFoundException();
			}
			result.put("productCount", company.getProductCount());
			result.put("productMax", company.getProductMax());
		}

		// �����������
		List<Group> specialGroupList = this.specialGroupDAO.findGroupList(product.getComId());
		if (specialGroupList.size() > 0) {
			Map<Integer, String> specialGroupMap = new HashMap<Integer, String>();
			String specialGroupIds = "," + StringUtil.toString(StringUtil.toStringArray(product.getSpecialGroupId()), ",") + ",";
			for (int i = 0; i < specialGroupList.size(); i++) {
				if (specialGroupIds.indexOf("," + specialGroupList.get(i).getGroupId() + ",") != -1) {
					specialGroupMap.put(specialGroupList.get(i).getGroupId(), specialGroupList.get(i).getGroupName());
				}
			}
			product.setSpecialGroupMap(specialGroupMap);
		}

		// �����ǩ
		if (product.getCatId() > 0) {
			product.setTagList(this.parseTags(product.getCatId(), this.getTags(product)));
		}

		int copyProId = 0;
		Map<Integer, Integer> copyProIdMap = this.tradeDAO.findTradeCopyProId(product.getComId());
		if (copyProIdMap.containsKey(product.getProId())) {
			copyProId = copyProIdMap.get(product.getProId());
		}

		result.put("product", product);
		result.put("copyProId", copyProId);
		return result;
	}

	/**
	 * ��֤�ؼ����Ƿ��ظ�
	 * @param params
	 * @return
	 * <pre>
	 * true �������ظ��ؼ���
	 * product.keywords.remote �ؼ����ظ�
	 * </pre>
	 */
	public String checkProductKeyword(QueryParams params) {
		if (StringUtil.isBlank(params.getKeyword1()) && StringUtil.isBlank(params.getKeyword2()) && StringUtil.isBlank(params.getKeyword3())) {
			return "true";
		}
		if (StringUtil.isNotBlank(params.getKeyword1())) {
			params.setQueryText(params.getKeyword1());
		}
		if (StringUtil.isNotBlank(params.getKeyword2())) {
			params.setQueryText(params.getKeyword2());
		}
		if (StringUtil.isNotBlank(params.getKeyword3())) {
			params.setQueryText(params.getKeyword3());
		}
		int proId = 0;
		if (params.getProId() != null) {
			proId = params.getProId()[0];
		}
		for (String keywords : this.productDAO.findProductKeywordList(params.getLoginUser().getComId(), proId)) {
			for (String keyword : StringUtil.split(keywords, ",")) {
				if (keyword.trim().equalsIgnoreCase(params.getQueryText())) {
					return "product.keywords.remote";
				}
			}
		}
		return "true";
	}
	/**
	 * ��֤�ؼ����Ƿ��ظ��������ϴ���Ʒʱ���ؼ����Ƿ����
	 * @param params
	 * @return
	 * <pre>
	 * true �������ظ��ؼ���
	 * product.keywords.remote �ؼ����ظ�
	 * </pre>
	 */
	public String checkProductKeywordForBacth(QueryParams params) {
		String tmpK1 = params.getKeyword1();
		String tmpK2 = params.getKeyword2();
		String tmpK3 = params.getKeyword3();
		int proId = 0;
		if (params.getProId() != null) {
			proId = params.getProId()[0];
		}
		for (String keywords : this.productDAO.findProductKeywordList(params.getLoginUser().getComId(), proId)) {
			for (String keyword : StringUtil.split(keywords, ",")) {
				if (keyword.trim().equalsIgnoreCase(tmpK1) || keyword.trim().equalsIgnoreCase(tmpK2) || keyword.trim().equalsIgnoreCase(tmpK3)) {
					return "product.keywords.remote";
				}
			}
		}
		return "true";
	}
	
	private boolean updateFoodPact(int comId) {
		UpdateMap companyExtra = new UpdateMap("CompanyExtra");
		companyExtra.addField("isFoodPact", 1);
		companyExtra.addWhere("comId", comId);
		return companyDAO.update(companyExtra) > 0;
	}
	
	/**
	 * ��ͨ��Ʒ����ύ
	 * @param response
	 * @param product
	 * @param loginUser
	 * @return
	 * <pre>
	 * product.specialGroupError: ��ʾ����������
	 * product.groupError: ��ʾ�������
	 * product.uploadFail: ��ʾ�ļ���ͼƬ�ϴ�ʧ��
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String addProduct(HttpServletResponse response, Product product, 
			LoginUser loginUser) {
		int comId = loginUser.getComId();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		product.setComId(comId);
		product.setUserId(loginUser.getUserId());
		
		// if product.rootCatId == 1 && !company.foodpact return error
		Category productCategory = CategoryUtil.getById(product.getCatId());
		if (productCategory.getRootId() == 1 && !company.isFoodPact()) {
			if (!product.isFoodPact()) {
				return "product.foodpact";
			} else {
				updateFoodPact(comId);
			}
		}
		
		if (company == null) {
			throw new PageNotFoundException();
		}
		// ��֤��Ʒ ����
		// ��Ʒ����
		if (company.getProductCount() >= company.getProductMax()) {
			return "product.full";
		}

		// ��������Ƿ����
		if (product.getSpecialGroupId() != null && product.getSpecialGroupId().length > 0) {
			if (!new GroupUtil(this.specialGroupDAO.findGroupList(product.getComId()))
					.check(product.getSpecialGroupId())) {
				return "product.specialGroupError";
			}
		}

		// �����Ƿ����
		String groupIds = ""; // ���������������ϼ����飬��������ʹ��
		int minGroupOrder = 100001; // Ĭ��ΪgroupOrderΪ0����groupId>0ʱ��Ĭ��Ϊ100000  
		if (product.getGroupId() > 0) {
			GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(comId));

			Group group = groupUtil.get(product.getGroupId());
			if (group == null || group.getChild() > 0) {
				return "product.groupError";
			}

			groupIds = groupUtil.getIdPath(product.getGroupId());

			minGroupOrder = this.productDAO.findMinGroupOrder(groupUtil, comId, group.getRootId());
		}

		// �ϴ�����
		if (product.getAttachment() != null) {
			try {
				Attachment att = UploadUtil.uploadFileStream(comId, 
						2, product.getAttachmentFileName(), 
						new FileInputStream(product.getAttachment()));
				if (att != null) {
					product.setFilePath(att.getFilePath().replace("\\", "/"));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		int tmpImgCount = 0;
		// �ϴ���ƷͼƬ
		Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
		// �ж��Ƿ񱣴���ˮӡ 
		boolean saveWatermark = false;
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			if (company.getImgCount() + tmpImgCount++ < company.getImgMax()) {
				Image image = product.getImage();
				if (!saveWatermark) {
					image.saveWatermark(response);
					saveWatermark = true;
				}
				image.setComId(comId);
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
				if(!number_pattern.matcher(map.get("imgType")).matches() 
						|| !"3".equals(map.get("imgType"))){
					map.put("imgType", "3");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgId(img.getImgId());
				product.setImgPath(img.getImgPath());
			}
		} 
		// TODO ��֤ ͼƬ��ַ����
		if (StringUtil.length(product.getImgPath()) >= 60) {
			return "product.uploadFail";
		}

		// �ϴ�����ͼƬ
		if (product.getProAddImg() != null && product.getProAddImg().length > 0) {
			String proAddImgs = "";
			for (String proAddImg : product.getProAddImg()) {
				map = UploadUtil.getImgParam(proAddImg);
				if (Boolean.parseBoolean(map.get("isUpload"))) {
					if (company.getImgCount() + tmpImgCount++ < company.getImgMax()) {
						Image image = product.getImage();
						if (!saveWatermark) {
							image.saveWatermark(response);
						}
						image.setComId(product.getComId());
						image.setImgName(map.get("imgName"));
						image.setImgPath(map.get("imgPath"));
						//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
						if(!number_pattern.matcher(map.get("imgType")).matches() 
								|| !"3".equals(map.get("imgType")) ){
							map.put("imgType", "3");
						}
						image.setImgType(Integer.parseInt(map.get("imgType")));
						com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
						if (img.getImgId() <= 0) {
							return "product.uploadFail";
						}
						proAddImgs += "," + img.getImgPath();
					}
				} else {
					proAddImgs += "," + proAddImg;
				}
			}
			product.setImgPaths(StringUtil.trimComma(proAddImgs));
		} else {
			product.setImgPaths("");
		}

		// ��Ӳ�Ʒ
		product.setKeywords(product.getInitKeywords());
		product.setListOrder(this.productDAO.findMinListOrder(comId) - 1);
		product.setGroupOrder(minGroupOrder - 1);
		product.setState(CN.STATE_WAIT);
		product.setTags(this.getTags(product));
		String currentTime = new DateUtil().getDateTime();
		product.setCreateTime(currentTime);
		product.setModifyTime(currentTime);
		product.setUpdateTime(currentTime);
		int proId = this.productDAO.addProduct(product);
		if (proId <= 0) {
			return "operateFail";
		}
		product.setDescription(ImageAlt.path(product.getDescription(), product.getProName()));
		this.productDAO.addProductExtra(product);

		// 2012-12-18 ȥ����Ʒ���鹦�� http://192.168.1.240:8080/browse/CN-285
		// �������
//		int tradeId = 0;
//		if (product.isAddTrade()) {
//			product.setCopyProId(proId);
//			product.setProType(CN.TRADE_SELL);
//			tradeId = this.tradeDAO.addTrade(product);
//			this.tradeDAO.addTradeExtra(product);
//
//			UserLog userLog = UserLogUtil.getUserLog(UserLog.TRADE, UserLog.ADD, 
//					"������顪��" + product.getProName(), loginUser);
//			tradeDAO.addUserLog(userLog);
//		}

		// ����CompanyExtra����
		UpdateMap updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("productCount", "+", 1);
		// ���������
//		if (tradeId > 0) {
//			updateMap.addField("tradeCount", "+", 1);
//		}
		
		// ���Ż���Ʒ
		if (StringUtil.isNotEmpty(product.getInitKeywords())) {
			updateMap.addField("optimizeProCount", "+", 1);
		}
		updateMap.addWhere("comId", comId);
		productDAO.update(updateMap);

		// ����Groups����
		if (product.getGroupId() > 0) {
			updateMap = new UpdateMap("Groups");
			updateMap.addField("productCount", "+", 1);
//			if (tradeId > 0) {
//				updateMap.addField("tradeCount", "+", 1);
//			}
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", groupIds, "in");
			productDAO.update(updateMap);
		}

		// ����SpecialGroup����
		if (product.getSpecialGroupId() != null && product.getSpecialGroupId().length > 0) {
			updateMap = new UpdateMap("SpecialGroup");
			updateMap.addField("productCount", "+", 1);
			updateMap.addWhere("comId", product.getComId());

			updateMap.addWhere("groupId", 
					StringUtil.toString(product.getSpecialGroupId(), ","), "in");
			
			productDAO.update(updateMap);

			this.specialGroupDAO.addSpecialProduct(comId, 
					new int[] { proId }, product.getSpecialGroupId());
		}

		UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.ADD, 
				"��Ӳ�Ʒ����" + product.getProName(), loginUser);
		productDAO.addUserLog(userLog);
		return "addSuccess";
	}

	/**
	 * ��Ʒ����ɾ��
	 * @param params 
	 * <pre>
	 * proId[]
	 * </pre>
	 * @return
	 * <pre>
	 * product.recycleFull: ����վ����
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String deleteProduct(QueryParams params) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		int[] proIdArr = params.getProId();
		
		if (params.getProId() == null) {
			return "operateFail";
		}

		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company == null) {
			throw new PageNotFoundException();
		}
		if (company.getProductDelCount() + proIdArr.length >= RECYCLE_MAX) {
			return "product.recycleFull";
		}

		// ��ѯӦ�ڸ���֮ǰ
		ListResult<Product> listResult = this.productDAO.findProductList(params);

		// �Ѳ�Ʒ�������վ
		String proIds = getProIdtoString(proIdArr);
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("state", CN.STATE_RECYCLE);
		updateMap.addField("catId", 0);
		updateMap.addField("groupId", 0);
		updateMap.addField("featureOrder", 0);
		updateMap.addField("featureGroupId", 0);
		updateMap.addField("keywords", "");
		updateMap.addField("videoId", 0);
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", proIds, "in");
		int updateNum = this.productDAO.update(updateMap);
		if (updateNum <= 0) {
			return "operateFail";
		}

		// ȡ��������Ĺ�����ϵ
		List<UpdateMap> updateMapList = new ArrayList<UpdateMap>();
		updateMap = new UpdateMap("Trade");
		updateMap.addField("copyProId", 0);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("copyProId", proIds, "in");
		updateMapList.add(updateMap);

		int productRejectNum = 0; // ���δͨ����Ʒ����
		int featureProNum = 0; // չ̨��Ʒ����
		int optimizeProNum = 0; // �Ż���Ʒ����
		
		GroupUtil groups = new GroupUtil(this.groupDAO.findGroupList(comId));
		for (Product product : listResult.getList()) {
			if (product.getState() == CN.STATE_REJECT) {
				productRejectNum++;
			}
			if (product.getFeatureOrder() > 0) {
				featureProNum++;
			}
			if (StringUtil.isNotEmpty(product.getKeywords())) {
				optimizeProNum++;
			}
			if (product.getGroupId() > 0 && (product.getState() == CN.STATE_WAIT 
					|| product.getState() == CN.STATE_PASS)) {
				updateMap = new UpdateMap("Groups");
				updateMap.addField("productCount", "-", 1);
				updateMap.addWhere("comId",comId);
				updateMap.addWhere("groupId", groups.getIdPath(product.getGroupId()), "in");
				updateMapList.add(updateMap);
			}
		}

		// �������������������
		List<Group> specialGroupList = this.specialGroupDAO.findGroupList(comId, proIdArr);
		for (Group group : specialGroupList) {
			updateMap = new UpdateMap("SpecialGroup");
			updateMap.addField("productCount", "-", 1);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", group.getGroupId());
			updateMapList.add(updateMap);
		}
		this.specialGroupDAO.deleteSpecialProduct(comId, getProIdtoString(proIdArr), null);

		// ����CompanyExtra
		//updateMapList = new ArrayList<UpdateMap>();
		updateMap = new UpdateMap("CompanyExtra");
		if (optimizeProNum > 0) {
			updateMap.addField("optimizeProCount", "-", optimizeProNum);
		}
		if (featureProNum > 0) {
			updateMap.addField("featureProCount", "-", featureProNum);
		}
		if (productRejectNum > 0) {
			updateMap.addField("productRejectCount", "-", productRejectNum);
		}
		updateMap.addField("productCount", "-", updateNum);
		updateMap.addField("productDelCount", "+", updateNum);
		updateMap.addWhere("comId", comId);
		updateMapList.add(updateMap);

		this.productDAO.update(updateMapList);

		UserLog userLog = null;
		for (Product product : listResult.getList()) {
			userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.RECYCLE, 
					"���ղ�Ʒ����" + product.getProName(), params.getLoginUser());
			productDAO.addUserLog(userLog);
		}

		return "deleteSuccess";
	}

	/**
	 * ��Ʒ�޸��ύ
	 * <pre>
	 * @param response 
	 * @param product 
	 * @param loginUser
	 * </pre>
	 * @return
	 * <pre>
	 * product.specialGroupError: ��ʾ����������
	 * product.groupError: ��ʾ�������
	 * product.uploadFail: ��ʾ�ļ���ͼƬ�ϴ�ʧ��
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateProduct(HttpServletResponse response, Product product, LoginUser loginUser) {
		int comId = loginUser.getComId();
		product.setComId(comId);
		
		Product pro = productDAO.findProduct(comId, product.getProId());
		product.setState(pro.getState());
		
		// ��������Ƿ����
		if (product.getSpecialGroupId() != null && product.getSpecialGroupId().length > 0) {
			if (!new GroupUtil(this.specialGroupDAO.findGroupList(product.getComId()))
					.check(product.getSpecialGroupId())) {
				return "product.specialGroupError";
			}
		}

		// ��֤����
		boolean isAddGroup = false;
		String groupIds = "";
		int minGroupOrder = 100001;
		GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(comId));
		if (product.getGroupId() > 0) {
			if (product.getOldGroupId() > 0) {
				Group oldGroup = groupUtil.get(product.getOldGroupId());
				if (oldGroup == null || oldGroup.getChild() > 0) {
					return "product.groupError";
				}
			}
			Group group = groupUtil.get(product.getGroupId());
			if (group == null || group.getChild() > 0) {
				return "product.groupError";
			}
			groupIds = groupUtil.getIdPath(product.getGroupId());

			minGroupOrder = this.productDAO.findMinGroupOrder(groupUtil, comId, group.getRootId());

			// ���δͨ���ͻ���վ״̬�����ݿ�ԭ�ȵ�groupIdΪ0��ֻҪ���ڷ���ID����������ӵ�
			if (product.getState() == CN.STATE_REJECT 
					|| product.getState() == CN.STATE_RECYCLE) {
				isAddGroup = true;
			}
		}

		// �ϴ�����
		if (product.getAttachment() != null) {
			try {
				Attachment att = UploadUtil.uploadFileStream(comId, 
						2, product.getAttachmentFileName(), 
						new FileInputStream(product.getAttachment()));
				if (att != null) {
					product.setFilePath(att.getFilePath().replace("\\", "/"));
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		int tmpImgCount = 0;
		Company company = this.companyDAO.findCompanyMemberType(comId);
		
		// �ϴ���ƷͼƬ
		Map<String, String> map = UploadUtil.getImgParam(product.getImgPath());
		boolean saveWatermark = false;
		if (Boolean.parseBoolean(map.get("isUpload"))) {
			// ͼƬδ�������ϴ�
			if (company.getImgCount() + tmpImgCount++ < company.getImgMax()) {
				Image image = product.getImage();
				if (!saveWatermark) {
					image.saveWatermark(response);
					saveWatermark = true;
				}
				image.setComId(comId);
				image.setImgName(map.get("imgName"));
				image.setImgPath(map.get("imgPath"));
				//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
				if(!number_pattern.matcher(map.get("imgType")).matches() 
						|| !"3".equals(map.get("imgType")) ){
					map.put("imgType", "3");
				}
				image.setImgType(Integer.parseInt(map.get("imgType")));
				com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
				if (img.getImgId() <= 0) {
					return "product.uploadFail";
				}
				product.setImgPath(img.getImgPath());
				product.setImgId(img.getImgId());
			}
		}
		// TODO ��֤ ͼƬ��ַ����
		if (StringUtil.length(product.getImgPath()) >= 60) {
			return "product.uploadFail";
		}
		
		// �ϴ�����ͼƬ
		if (product.getProAddImg() != null && product.getProAddImg().length > 0) {
			String proAddImgs = "";
			for (String proAddImg : product.getProAddImg()) {
				// ͼƬδ�������ϴ�
				map = UploadUtil.getImgParam(proAddImg);
				if (Boolean.parseBoolean(map.get("isUpload"))) {
					if (company.getImgCount() + tmpImgCount++ < company.getImgMax()) {
						Image image = product.getImage();
						if (!saveWatermark) {
							image.saveWatermark(response);
						}
						image.setComId(comId);
						image.setImgName(map.get("imgName"));
						image.setImgPath(map.get("imgPath"));
						//�жϲ���һλ���־����ó�3��3Ϊ��Ʒ����ͼƬ
						if(!number_pattern.matcher(map.get("imgType")).matches() 
								|| !"3".equals(map.get("imgType")) ){
							map.put("imgType", "3");
						}
						image.setImgType(Integer.parseInt(map.get("imgType")));
						com.hisupplier.commons.entity.Image img = UploadUtil.swfUploadImage(image);
						if (img.getImgId() <= 0) {
							return "product.uploadFail";
						}
						proAddImgs += "," + img.getImgPath();
					}
				} else {
					proAddImgs += "," + proAddImg;
				}
			}
			//���Ǳ���֤��������ͼƬ�����ϴ�����ʱ��ֹProduct����getImgPaths��ȡ������ͼƬ
			product.setFormValidate(false);
			product.setImgPaths(StringUtil.trimComma(proAddImgs));
		} else {
			product.setImgPaths("");
		}

		// ���²�Ʒ
		int oldState = product.getState();  //ִ�и���ǰ��״̬
		int newState = 0;

		product.setKeywords(product.getInitKeywords());
		String currentTime = new DateUtil().getDateTime();
		product.setTags(this.getTags(product));
		product.setGroupOrder(minGroupOrder - 1);
		product.setModifyTime(currentTime);
		product.setUpdateTime(currentTime);
		// �ƽ��Ա���ͨ�����޸ĺ� ��Ϊ"�������"
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
		product.setState(newState);
		
		product.setGroupIdBak(0);
		product.setComId(comId);
		if (this.productDAO.updateProduct(product) <= 0) {
			return "operateFail";
		}

		UpdateMap updateMap = new UpdateMap("ProductExtra");
		updateMap.addField("description", ImageAlt.path(product.getDescription(), product.getProName()));
		updateMap.addField("tags", this.getTags(product));
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", product.getProId());
		productDAO.update(updateMap);

		// ȡ��������Ĺ�����ϵ
		updateMap = new UpdateMap("Trade");
		updateMap.addField("copyProId", 0);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("copyProId", product.getProId());
		productDAO.update(updateMap);

		// ����ı䡣�¾ɷ���ID��ͬ���жϷ���ı�ֻ��״̬Ϊ�ȴ���˺����ͨ����Ч
		if (product.getGroupId() != product.getOldGroupId() 
				&& (oldState == CN.STATE_REJECT_WAIT || oldState == CN.STATE_PASS || oldState == CN.STATE_WAIT)) {
			if (product.getGroupId() > 0) {
				isAddGroup = true;
			}
			if (product.getOldGroupId() > 0) {
				updateMap = new UpdateMap("Groups");
				updateMap.addField("productCount", "-", 1);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("groupId", groupUtil.getIdPath(product.getOldGroupId()), "in");
				productDAO.update(updateMap);
			}
		}

		if (isAddGroup) {
			updateMap = new UpdateMap("Groups");
			updateMap.addField("productCount", "+", 1);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("groupId", groupIds, "in");
			productDAO.update(updateMap);
		}

		if (!StringUtil.equalsValues(StringUtil.toStringArray(product.getSpecialGroupId()), 
				StringUtil.toStringArray(product.getOldSpecialGroupId()))) {
			// ���ٵ��������
			String differGroupId = this.getDifferArray(product.getOldSpecialGroupId(), 
					product.getSpecialGroupId());
			if (product.getOldSpecialGroupId() != null 
					&& product.getOldSpecialGroupId().length > 0 
					&& StringUtil.isNotEmpty(differGroupId) 
					&& (oldState == CN.STATE_REJECT_WAIT || oldState == CN.STATE_PASS || oldState == CN.STATE_WAIT)) {
				updateMap = new UpdateMap("SpecialGroup");
				updateMap.addField("productCount", "-", 1);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("groupId", differGroupId, "in");
				productDAO.update(updateMap);
				this.specialGroupDAO.deleteSpecialProduct(product.getComId(), 
						product.getProId() + "", null);
			}
			// ���ӵ��������
			differGroupId = this.getDifferArray(product.getSpecialGroupId(), 
					product.getOldSpecialGroupId());
			if (product.getSpecialGroupId() != null 
					&& product.getSpecialGroupId().length > 0 
					&& StringUtil.isNotEmpty(differGroupId)) {
				updateMap = new UpdateMap("SpecialGroup");
				updateMap.addField("productCount", "+", 1);
				updateMap.addWhere("comId", comId);
				updateMap.addWhere("groupId", differGroupId, "in");
				productDAO.update(updateMap);

				this.specialGroupDAO.addSpecialProduct(comId, 
						new int[] { product.getProId() }, 
						StringUtil.toIntArray(StringUtil.toArray(differGroupId, ",")));
			}
		}

		// ����CompanyExtra����
		updateMap = new UpdateMap("CompanyExtra");
		if (oldState == CN.STATE_RECYCLE) {
			updateMap.addField("productCount", "+", 1);
			updateMap.addField("productDelCount", "-", 1);
		}
		if (oldState == CN.STATE_REJECT) {
			updateMap.addField("productRejectCount", "-", 1);
		}
		// ԭ�ȵĹؼ���Ϊ�գ���ǰ��д�˹ؼ���
		if (StringUtil.isEmpty(product.getOldKeyword()) 
				&& StringUtil.isNotEmpty(product.getInitKeywords())) {
			updateMap.addField("optimizeProCount", "+", 1);
		}
		// ��ǰ�Ĺؼ���Ϊ�գ�ԭ�ȵĹؼ��ʲ�Ϊ��
		if (!StringUtil.isEmpty(product.getOldKeyword()) 
				&& StringUtil.isEmpty(product.getInitKeywords())) {
			updateMap.addField("optimizeProCount", "-", 1);
		}
		updateMap.addWhere("comId", comId);
		if (updateMap.getFields().size() > 0) {
			productDAO.update(updateMap);
		}

		UserLog userLog = null;
		if (oldState == CN.STATE_RECYCLE) {
			userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.REUSE, 
					"��ԭ��Ʒ����" + product.getProName(), loginUser);
		} else {
			userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, 
					"�޸Ĳ�Ʒ����" + product.getProName(), loginUser);
		}
		productDAO.addUserLog(userLog);
		
		if (oldState == 20 || oldState == 14) { //���ͨ������Ϣ����������޸��ύ����ֱ�Ӱ�ԭ����ʾ���޸ĳɹ���
			return "oldSuccess";
		}else {
			return "productSuccess"; //�µ��ύ�ɹ�ҳ��
		}
	}

	/**
	 * ��Ʒ����
	 * @param params 
	 * <pre>
	 * loginUser.comId 
	 * proId[] 
	 * userId
	 * </pre>
	 * @return 
	 * <pre>
	 * operateFail
	 * product.allotSuccess
	 * </pre>
	 */
	public String updateProductUser(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		// TODO ��֤�û��Ƿ�����ڸù�˾
		int userId = params.getUserId();
		User user = this.userDAO.findUser(userId, comId);
		if (user == null) return "operateFail";
		
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("userId", userId);
		updateMap.addField("updateTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", getProIdtoString(params.getProId()), "in");
		if (this.productDAO.update(updateMap) > 0) {
			ListResult<Product> listResult = this.productDAO.findProductList(params);
			UserLog userLog = null;
			for (Product product : listResult.getList()) {
				userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, 
					"���ɲ�Ʒ��" + product.getProName() + "�������ʺ�" + user.getEmail(), 
					params.getLoginUser());
				productDAO.addUserLog(userLog);
			}
			return "product.allotSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ��Ʒ����
	 * @param params
	 * <pre>
	 * loginUser.comId 
	 * proId[]
	 * </pre>
	 * @return
	 * <pre>
	 * product.selectOne
	 * operateFail
	 * product.repostSuccess
	 * </pre>
	 */
	public String updateProductRepost(QueryParams params) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		if (params.getProId() == null || params.getProId().length < 1) {
			return "product.selectOne";
		}
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", getProIdtoString(params.getProId()), "in");
		if (this.productDAO.update(updateMap) > 0) {
			ListResult<Product> listResult = this.productDAO.findProductList(params);
			UserLog userLog = null;
			for (Product product : listResult.getList()) {
				userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, 
						"���·�����Ʒ����" + product.getProName(), params.getLoginUser());
				productDAO.addUserLog(userLog);
			}
			return "product.repostSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ���в�Ʒ����
	 * @param params
	 * <pre>
	 * loginUser.comId 
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * product.repostAllSuccess
	 * </pre>
	 */
	public String updateProductRepostAll(QueryParams params) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("state", "10, 15, 20", "in");
		if (this.productDAO.update(updateMap) > 0) {
			UserLog userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.MODIFY, 
					"���·������в�Ʒ", params.getLoginUser());
			productDAO.addUserLog(userLog);
			return "product.repostAllSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ������Ʒ���򣬰������в�Ʒ�����ڲ�Ʒ����
	 * <pre>
	 * ������Ʒ�������ӣ�
	 * ��Ʒ ID[11,22,33,44] ����ֵ[1,2,4,5] �������ƷID[44]��params.getProId()[0]�� λ��2��params.getListOrder()��
	 * 1) ��ѯλ����2�Ĳ�Ʒ[22]����ֵ��2
	 * 2) ��ֵ��ƷID[44]������ֵ5 -> 2
	 * 3) ��ID[22,33]������ֵ�ֱ��1��2 -> 3��4 -> 5 ��
	 * 
	 * �������Ʒ ID[11,22,33,44] ����ֵ[1,3,5,2]��ҳ����ʾΪ��ƷID[11,44,22,33]
	 * </pre>
	 * @param params loginUser.comId listOrder groupId>0���ڲ�Ʒ���� groupId=0���в�Ʒ����
	 * @return
	 * <pre>
	 * product.groupError:�������
	 * operateFail������ʧ��
	 * orderSuccess������ɹ�
	 * </pre>
	 */
	public String updateProductSingleOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		if (params.getListOrder() < 1) {
			return "operateFail";
		}
		// ������λ�ô�0��ʼ��λ��ֵ���1
		int newOrder = params.getListOrder() - 1; 

		String groupIds = null;
		String updateField = "listOrder";
		if (params.getGroupId() > 0) {
			GroupUtil groupUtil = new GroupUtil(this.groupDAO.findGroupList(comId));
			Group group = groupUtil.get(params.getGroupId());
			if (group == null || group.getParentId() > 0) {
				return "product.groupError";
			}
			groupIds = groupUtil.getChildIds(group.getRootId());
			updateField = "groupOrder";
		}

		List<Integer> proIdList = this.productDAO.findProductIdList(comId, groupIds, -1);

		int proId = 0; // Ŀ��λ�õĲ�Ʒ����ֵ
		String proIds = "";
		String operator = "";
		int oldOrder = 0;
		for (int i = 0; i < proIdList.size(); i++) {
			if (params.getProId()[0] == proIdList.get(i)) {
				oldOrder = i;
			}
			if (i == newOrder) {
				proId = proIdList.get(i);
			}
		}
		if (oldOrder < newOrder) {
			for (int i = oldOrder + 1; i <= newOrder; i++) {
				proIds += "," + proIdList.get(i);
			}
			operator = "-";
		} else {
			for (int i = newOrder; i < oldOrder; i++) {
				proIds += "," + proIdList.get(i);
			}
			operator = "+";
		}
		Product product = this.productDAO.findProduct(comId, proId);
		if (product == null) {
			return "operateFail";
		}

		int listOrder = product.getListOrder();
		if (params.getGroupId() > 0) {
			listOrder = product.getGroupOrder();
		}
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField(updateField, listOrder);
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", params.getProId()[0]);
		this.productDAO.update(updateMap);

		if (!StringUtil.isEmpty(proIds)) {
			updateMap = new UpdateMap("Product");
			updateMap.addField(updateField, operator, 1);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("proId", StringUtil.trimComma(proIds), "in");
		}

		if (this.productDAO.update(updateMap) > 0) {
			return "orderSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ҳ���ڲ�Ʒ����
	 * @param params 
	 * <pre>
	 * loginUser.comId 
	 * proId[]
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateProductOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		int listOrder = params.getPageNo() > 0 
			? (params.getPageNo() - 1) * params.getPageSize() + 100000 : 100000;
		List<UpdateMap> updateMapList = new ArrayList<UpdateMap>();

		for (int proId : params.getProId()) {
			UpdateMap updateMap = new UpdateMap("Product");
			if (params.getGroupId() > 0) {
				updateMap.addField("groupOrder", listOrder++);
			} else {
				updateMap.addField("listOrder", listOrder++);
			}
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("proId", proId);
			updateMapList.add(updateMap);
		}
		if (this.productDAO.update(updateMapList) > 0) {
			return "orderSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ѡ��չ̨��Ʒ
	 * @param params 
	 * <pre>
	 * loginUser.comId
	 * proId[0]
	 * </pre>
	 * @return
	 * <pre>
	 * product.featureFull��չ̨��Ʒ����
	 * operateFail
	 * addSuccess
	 * </pre>
	 */
	public String updateFeatureAdd(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		Company company = this.companyDAO.findCompanyMemberType(comId);
		if (company.getFeatureProCount() >= company.getFeatureProMax()) {
			return "product.featureFull";
		}

		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("featureOrder", this.productDAO.findMaxFeatureOrder(comId) + 1);
		updateMap.addField("updateTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", params.getProId()[0]);
		if (this.productDAO.update(updateMap) <= 0) {
			return "operateFail";
		}

		updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("featureProCount", "+", 1);
		updateMap.addWhere("comId", comId);
		this.productDAO.update(updateMap);

		return "addSuccess";
	}

	/**
	 * ����չ��
	 * @param params 
	 * <pre>
	 * loginUser.comId
	 * proId[0]
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String updateFeatureRemove(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("featureOrder", 0);
		updateMap.addField("featureGroupId", 0);
		updateMap.addField("updateTime", new DateUtil().getDateTime());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", params.getProId()[0]);
		updateMap.addWhere("featureOrder", 0, ">");
		if (this.productDAO.update(updateMap) <= 0) {
			return "operateFail";
		}
		updateMap = new UpdateMap("CompanyExtra");
		updateMap.addField("featureProCount", "-", 1);
		updateMap.addWhere("comId", comId);
		this.productDAO.update(updateMap);
		return "deleteSuccess";
	}

	/**
	 * չ̨��Ʒ���ӵ���Ʒ��groupId=0��/���飨groupId>0��
	 * @param params 
	 * <pre>
	 * loginUser.comId 
	 * proId[0] 
	 * groupId
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * editSuccess
	 * </pre>
	 */
	public String updateFeatureSet(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("featureGroupId", params.getGroupId());
		updateMap.addWhere("comId", comId);
		updateMap.addWhere("proId", params.getProId()[0]);
		if (this.productDAO.update(updateMap) <= 0) {
			return "operateFail";
		}
		return "editSuccess";
	}

	/**
	 * չ̨��Ʒ����
	 * @param params 
	 * <pre>
	 * loginUser.comId
	 * proId[]
	 * </pre>
	 * @return
	 * <pre>
	 * operateFail
	 * orderSuccess
	 * </pre>
	 */
	public String updateFeatureOrder(QueryParams params) {
		int comId = params.getLoginUser().getComId();
		if (params.getListOrder() < 1 || params.getOldOrder() < 1) {
			return "operateFail";
		}
		// ������λ�ô�0��ʼ��λ��ֵ���1
		int newOrder = params.getListOrder() - 1; 
		int oldOrder = params.getOldOrder() - 1;

		if (params.getProId().length < (newOrder > oldOrder ? newOrder : oldOrder)) {
			return "operateFail";
		}

		// ����
		int proId = params.getProId()[newOrder];
		params.getProId()[newOrder] = params.getProId()[oldOrder];
		params.getProId()[oldOrder] = proId;

		List<UpdateMap> updateMapList = new ArrayList<UpdateMap>();
		int featureOrder = 1;
		for (int id : params.getProId()) {
			UpdateMap updateMap = new UpdateMap("Product");
			updateMap.addField("featureOrder", featureOrder++);
			updateMap.addWhere("comId", comId);
			updateMap.addWhere("proId", id);
			updateMapList.add(updateMap);
		}
		if (this.productDAO.update(updateMapList) > 0) {
			return "orderSuccess";
		} else {
			return "operateFail";
		}
	}

	/**
	 * ����վɾ����Ʒ
	 * @param params 
	 * <pre>
	 * loginUser.comId
	 * proId[]
	 * </pre>
	 * @param deleteAll �Ƿ���� ��ֹ������������ĸ��´���
	 * @return
	 * <pre>
	 * operateFail
	 * deleteSuccess
	 * </pre>
	 */
	public String updateRecycleDelete(QueryParams params, boolean deleteAll) {
		String currentTime = new DateUtil().getDateTime();
		int comId = params.getLoginUser().getComId();
		UpdateMap updateMap = new UpdateMap("Product");
		updateMap.addField("state", CN.STATE_DELETE);
		updateMap.addField("modifyTime", currentTime);
		updateMap.addField("updateTime", currentTime);
		updateMap.addField("imgId", 0);
		updateMap.addField("imgPath", "");
		updateMap.addWhere("state", CN.STATE_RECYCLE);
		updateMap.addWhere("comId", comId);
		if (!deleteAll) {
			updateMap.addWhere("proId", getProIdtoString(params.getProId()), "in");
		}
		int updateNum = this.productDAO.update(updateMap);
		if (updateNum <= 0) {
			return "operateFail";
		}

		updateMap = new UpdateMap("CompanyExtra");
		UserLog userLog = null;
		if (deleteAll) {
			updateMap.addField("productDelCount", 0);
			userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.DELETE, 
					"��ջ���վ", params.getLoginUser());
		} else {
			updateMap.addField("productDelCount", "-", updateNum);
			userLog = UserLogUtil.getUserLog(UserLog.PRODUCT, UserLog.DELETE, 
					"����ɾ������վ��Ʒ", params.getLoginUser());
		}

		updateMap.addWhere("comId", comId);
		this.productDAO.update(updateMap);

		productDAO.addUserLog(userLog);
		return "deleteSuccess";
	}

	/**
	 * ��ջ���վ��Ʒ
	 * @param params
	 * <pre>
	 * loginUser.comId
	 * </pre>
	 * @return 
	 */
	public String updateRecycleEmpty(QueryParams params) {
		return this.updateRecycleDelete(params, true);
	}

	/**
	 * ���²�Ʒ�Ƿ���ʾNewͼ��
	 * @param params
	 * @return operateFail
	 * addSuccess
	 */
	public String updateProductIco(QueryParams params) {
		UpdateMap updateGroup = new UpdateMap("Product");
		if (params.isShowNewIco()) {
			updateGroup.addField("isShowNewIco", 1);
		} else {
			updateGroup.addField("isShowNewIco", 0);
		}
		updateGroup.addWhere("comId", params.getLoginUser().getComId());
		updateGroup.addWhere("proId", params.getTmpProdId());

		if (this.productDAO.update(updateGroup) <= 0) {
			return "operateFail";
		}
		return "addSuccess";
	}
	
	public Map<String,Object> productSuccess(QueryParams params) {
		Map<String, Object> result = new HashMap<String, Object>();
		Product product = this.productDAO.findProduct(params.getLoginUser().getComId(), params.getProId()[0]);
		if (product == null) {
			throw new PageNotFoundException();
		}
		if (params.getResultType() == 0) {
			result.put("addMessage", "��ϲ��������Ϣ���ύ�ɹ���");
		}else {
			result.put("addMessage", "��ϲ��������Ϣ���޸ĳɹ���");
		}
		return result;
	}

	/**
	 * ��ϱ�ǩ
	 * @param product
	 * @return
	 */
	private String getTags(Product product) {
		String tags = "";
		if (product.getTagName1() != null) {
			for (int i = 0; i < product.getTagName1().length; i++) {
				tags += product.getTagName1()[i] + "-" + product.getTagValue1()[i] + ",";
			}
		}
		tags = StringUtil.trimComma(tags) + ";";
		if (product.getTagName2() != null) {
			for (int i = 0; i < product.getTagName2().length; i++) {
				tags += product.getTagName2()[i] + "-" + product.getTagValue2()[i] + ",";
			}
		}
		tags = StringUtil.trimComma(tags) + ";";
		if (product.getTagName3() != null) {
			for (int i = 0; i < product.getTagName3().length; i++) {
				if(!"������������".equals(product.getTagName3()[i]) 
						||!"����������ֵ".equals(product.getTagValue3()[i])){
					tags += product.getTagName3()[i] + "-" + product.getTagValue3()[i] + ",";
				}
				
			}
		}
		return StringUtil.trimComma(tags);
	}

	/**
	 * �ֽ��ǩ
	 * @param product
	 * @return
	 */
	private List<Tag> parseTags(int catId, String tags) {
		List<Tag> tagResultList = new ArrayList<Tag>();
		List<Tag> tag1List = new ArrayList<Tag>();
		List<Tag> tag2List = new ArrayList<Tag>();
 
		// ���б�ǩ�б�
		List<Tag> tagList = this.productDAO.findTagList(catId); 
		// ѡ�б�ǩ�б�
		List<Tag> tagValueList = Tag.parseTags(tags, true); 

		try {
			// ��map����ǩ���ǩֵ���Ϊһ�Զ�Ĺ�ϵ
			Map<Integer, Tag> tagMap = new HashMap<Integer, Tag>();
			for (Tag oldTag : tagList) {
				if (StringUtil.isEmpty(oldTag.getTagValueName())) {
					Tag tag = new Tag();
					PropertyUtils.copyProperties(tag, oldTag);
					for (Tag tag2 : tagValueList) {
						if (oldTag.getTagId() == tag2.getTagId()) {
							tag.setTagValueName(tag2.getTagValueName());
						}
//						System.out.println("---" + tag2.getTagValueId() + oldTag.getTagValueName());
						oldTag.setTagValueId(tag2.getTagValueId());
//						System.out.println("name: " + tag2.getTagValueName());
					}
//					if (StringUtil.isBlank(oldTag.getUnit())) {
//						System.out.println("sss- : " + tag.getTagValueName());
//					}
					tag.setUnit(oldTag.getUnit());
					tag.setType(2);
					tag2List.add(tag);
				} else {
//					System.out.println("else");
					Tag tag = tagMap.get(oldTag.getTagId());
					TagValue tagValue = new TagValue();
					tagValue.setValueId(oldTag.getTagValueId());
					tagValue.setValueName(oldTag.getTagValueName());
					if (tag == null) {
						tag = new Tag();
						PropertyUtils.copyProperties(tag, oldTag);
						tag.setTagValueList(new ArrayList<TagValue>());
						tag.getTagValueList().add(tagValue);
						System.out.println("id: " +tag.getTagValueId());
//						tag.setTagValueId(0);
//						tag.setTagValueName("");
						tag.setType(1);
						for (Tag tag1 : tagValueList) {
							if (tag.getTagId() == tag1.getTagId()) {
								tag.setTagValueId(tag1.getTagValueId());
							}
						}
						tagMap.put(tag.getTagId(), tag);
					} else {
						tag.getTagValueList().add(tagValue);
					}
				}
			}
			Iterator<Tag> its = tagMap.values().iterator();
			while (its.hasNext()) {
				tag1List.add(its.next());
			}
		} catch (Exception e) {
		}

		tagResultList.addAll(tag1List);
		tagResultList.addAll(tag2List);

		for (Tag tag3 : tagValueList) {
			if (tag3.getType() == 3) {
				if (tag3.getUnit() == null) {
					tag3.setUnit("");
				}
				tagResultList.add(tag3);
			}
		}

		return tagResultList;
	}

	/**
	 * �ֽ�ؼ���
	 * @param product
	 * @return
	 */
	private Product parseKeywords(Product product) {
		String[] keywords = StringUtil.toArray(product.getKeywords(), ",");
		int length = keywords.length;
		product.setKeyword1(length > 0 ? keywords[0] : "");
		product.setKeyword2(length > 1 ? keywords[1] : "");
		product.setKeyword3(length > 2 ? keywords[2] : "");
		return product;
	}

	
	/**
	 * �����һ��˵�����
	 * @param secondSheet
	 * @param product
	 */
	private void processFirstSheet(WritableSheet firstSheet,Product product){
		try {
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 11, 
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
			
			WritableCellFormat wcf_row4_gold = new WritableCellFormat();
			wcf_row4_gold.setFont(wf);
			wcf_row4_gold.setBackground(Colour.GOLD);
			wcf_row4_gold.setAlignment(Alignment.CENTRE);
			wcf_row4_gold.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_row4_gold.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			
			WritableCellFormat wcf_row4_rose = new WritableCellFormat();
			wcf_row4_rose.setFont(wf);
			wcf_row4_rose.setBackground(Colour.ROSE);
			wcf_row4_rose.setAlignment(Alignment.CENTRE);
			wcf_row4_rose.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_row4_rose.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		
			WritableCellFormat wcf_row2 = new WritableCellFormat();
			wcf_row2.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);

			
			processTagColumn(firstSheet, product);//�����Զ����ǩ��
			
			WritableCellFeatures wcf_A4 = new WritableCellFeatures();
			wcf_A4.setComment("Hisupplier:��Ʒ���Ʋ�����120���ַ�");
			WritableCellFeatures wcf_B4 = new WritableCellFeatures();
			wcf_B4.setComment("Hisupplier:�ؼ��ʲ����ظ�");
			WritableCellFeatures wcf_F4 = new WritableCellFeatures();
			wcf_F4.setComment("Hisupplier:��ƷժҪ������150���ַ���");
			
			//�ϲ���
			firstSheet.mergeCells(0, 1, 3, 1);
			
			//���������ֶ���
			firstSheet.addCell(new Label(0, 0, product.getCatId()+""));
			firstSheet.addCell(new Label(0, 1, product.getCatName(), wcf_row2));
			firstSheet.addCell(new Label(0, 2, "proName"));
			firstSheet.addCell(new Label(1, 2, "keyword1"));
			firstSheet.addCell(new Label(2, 2, "imgPath"));
			firstSheet.addCell(new Label(3, 2, "brief"));
			firstSheet.addCell(new Label(4, 2, "description"));
			
			//���ñ�ǩ��ʾ
			Label A4 = new Label(0, 3, "*��Ʒ����", wcf_row4_gold);
			Label B4 = new Label(1, 3, "��Ʒ�ؼ���", wcf_row4_gold);
			Label F4 = new Label(3, 3, "*��ƷժҪ", wcf_row4_rose);
			A4.setCellFeatures(wcf_A4);
			B4.setCellFeatures(wcf_B4);
			F4.setCellFeatures(wcf_F4);
			
			firstSheet.addCell(A4);
			firstSheet.addCell(B4);
			firstSheet.addCell(new Label(2, 3, "��ƷͼƬ", wcf_row4_gold));
			firstSheet.addCell(F4);
			firstSheet.addCell(new Label(4, 3, "��ϸ����", wcf_row4_rose));
			
			//����ͼƬ
			if (product.isShowSetpOne() && product.getProImgArray() != null 
					&& product.getProImgArray().length > 0) {
				String[] imgArray = product.getProImgArray();
				String[] imgNameArray = new String[imgArray.length];
				String paths = "";//�����ֶΣ����������ݿ�ʱ����
				for (int i=0;i< imgArray.length  ;i++) {
					imgNameArray[i] = Coder.decodeURL(imgArray[i].replaceAll("%", "").split(";")[0].split(":")[1]);
					paths +="," + imgArray[i];
				}
				firstSheet.addCell(new Label(2, 0, paths.substring(1,paths.length())));
				for (int k=0;k< firstSheet.getRows();k++) {
					firstSheet.getWritableCell(2, 4+k).setCellFeatures(addColumnList(imgNameArray));
				}
			}
			
			//�����иߺ��п�
			firstSheet.setRowView(0, 0);//���ص�һ��
			firstSheet.setRowView(1, 600);
			firstSheet.setRowView(2, 0);//���ص�����
			firstSheet.setRowView(3, 500);
			firstSheet.setColumnView(0, 20);
			firstSheet.setColumnView(1, 20);
			firstSheet.setColumnView(2, 20);
			firstSheet.setColumnView(3, 20);
			firstSheet.setColumnView(4, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ����ڶ���˵�����
	 * @param secondSheet
	 */
	private void processSecondSheet(WritableSheet secondSheet){
		try {
			StringBuffer text = new StringBuffer();
			text.append("\n1. ע���벻Ҫ�����޸��б��⡢��˳������ݣ��⽫���²�Ʒ�����ϴ���ʧ�ܡ�\n");
			text.append("2. * ��ǵ���Ϊ���������2�У�Ϊ��Ʒ���ơ���ƷժҪ���ޱ�ǵ�Ϊѡ����û��ɸ�����Ҫѡ�\n");
			text.append("3. ��Ʒ��Ϣ�ķ�Ϊ4�֣���ɫΪ��Ʒ������أ���ɫΪ��Ʒ������أ���ɫΪ��Ʒ������أ���ɫΪ������ء�\n");
			text.append("4. ��Ʒ��Ϣ�����Ͻ��к�ɫ�����α�ǵı�ʾ����ʾ���ݣ��û���������������б��⴦���в鿴��\n");
			
			WritableCellFormat wcf = new WritableCellFormat();
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, 
					false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);
			wcf.setFont(wf);
			wcf.setBackground(Colour.GOLD);
			wcf.setVerticalAlignment(VerticalAlignment.CENTRE);
			
			WritableCellFormat wcf2 = new WritableCellFormat();
			wcf2.setWrap(true); 
			Label A1 = new Label(0, 0, "��Ʒ��Ϣ�༭�淶", wcf);
			Label A2 = new Label(0, 1, text.toString(), wcf2);
			Label A3 = new Label(0, 2, "�����淶", wcf);
			Label A4 = new Label(0, 3, "\nϵͳֻĬ��������Ʒ��Ϣ�ı༭�ڵ�һ��Sheetҳ�У�" +
					"�û������������Ӻͱ༭�µ�\nSheetҳ�����򽫲���ϵͳ���ܡ�", wcf2);
			
			secondSheet.addCell(A1);
			secondSheet.addCell(A2);
			secondSheet.addCell(A3);
			secondSheet.addCell(A4);
			
			secondSheet.setRowView(0, 300);
			secondSheet.setRowView(1, 1400);
			secondSheet.setRowView(2, 300);
			secondSheet.setRowView(3, 600);
			secondSheet.setColumnView(0, 100);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * �����Զ����ǩ��
	 * @param firstSheet
	 * @param product
	 */
	private void processTagColumn(WritableSheet firstSheet, Product product){
		try{
			WritableFont wf = new WritableFont(WritableFont.ARIAL, 11, 
					WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
			WritableCellFormat wcf_row4_yellow = new WritableCellFormat();
			wcf_row4_yellow.setFont(wf);
			wcf_row4_yellow.setBackground(Colour.YELLOW2);
			wcf_row4_yellow.setAlignment(Alignment.CENTRE);
			wcf_row4_yellow.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_row4_yellow.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			
			List<Tag> tagList = this.parseTags(product.getCatId(), null);
			int tagListCount = 0;
			WritableCellFormat wcf_all_common = new WritableCellFormat();
			wcf_all_common.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			if (tagList != null && tagList.size() > 0) {
				tagListCount = tagList.size();
			}
			//��ʼ����Ԫ�񣨱߿�
			for (int i=0; i<ROW_RECORD; i++) {
				for (int j=0; j<9+tagListCount; j++) {
					firstSheet.addCell(new Blank(j,i,wcf_all_common));
				}
			}
			WritableCellFormat wcf_row4_blue = new WritableCellFormat();
			wcf_row4_blue.setFont(wf);
			wcf_row4_blue.setBackground(Colour.PALE_BLUE);
			wcf_row4_blue.setAlignment(Alignment.CENTRE);
			wcf_row4_blue.setVerticalAlignment(VerticalAlignment.CENTRE);
			wcf_row4_blue.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
			
			WritableCellFeatures wcf_price = new WritableCellFeatures();
			wcf_price.setComment("Hisupplier:�۸���ʽ��10.02~20.02");
			
			firstSheet.addCell(new Label(tagListCount+5, 2, "minOrderNum"));
			firstSheet.addCell(new Label(tagListCount+6, 2, "minOrderUnit"));
			firstSheet.addCell(new Label(tagListCount+7, 2, "price"));
			firstSheet.addCell(new Label(tagListCount+8, 2, "transportation"));
			
			Label priceLabel = new Label(tagListCount+7, 3, "�۸�Ԫ��", wcf_row4_blue);
			priceLabel.setCellFeatures(wcf_price);
			firstSheet.addCell(new Label(tagListCount+5, 3, "��С����", wcf_row4_blue));
			firstSheet.addCell(new Label(tagListCount+6, 3, "������λ", wcf_row4_blue));
			firstSheet.addCell(priceLabel);
			firstSheet.addCell(new Label(tagListCount+8, 3, "���䷽ʽ", wcf_row4_blue));
			if(tagList != null && tagList.size() > 0){
				tagListCount = tagList.size();
				firstSheet.setColumnView(tagListCount+5, 20);
				firstSheet.setColumnView(tagListCount+6, 20);
				firstSheet.setColumnView(tagListCount+7, 20);
				firstSheet.setColumnView(tagListCount+8, 20);

				for(int i= 0;i<tagListCount;i++){
					Tag tag = tagList.get(i);
					if(StringUtil.isNotEmpty(tag.getUnit())){
						firstSheet.addCell(new Label(i+5, 3, 
								tag.getTagName() + "(" + tag.getUnit() + ")", wcf_row4_yellow));
						firstSheet.setColumnView(i+5, (tag.getTagName() + tag.getUnit()).length()*3);
					}else{
						firstSheet.addCell(new Label(i+5, 3, tag.getTagName(), wcf_row4_yellow));
						firstSheet.setColumnView(i+5, tag.getTagName().length()*4);
					}
					List<TagValue> tagValueList = tag.getTagValueList();
					if(tagValueList != null && tagValueList.size() > 0){
						firstSheet.addCell(new Label(i+5, 2, "attr_tagName1_" + tag.getTagId()));
						String[] attrValueArray = new String[tagValueList.size()];
						String tagValueIds = "";
						for(int j= 0;j<tagValueList.size();j++){
							attrValueArray[j] = tagValueList.get(j).getValueName();
							tagValueIds += ";" + tagValueList.get(j).getValueName() + 
									"," + tagValueList.get(j).getValueId();
						}
						firstSheet.addCell(new Label(i+5, 0, tagValueIds.substring(1,tagValueIds.length())));
						for(int k=0;k< firstSheet.getRows();k++){
							firstSheet.getWritableCell(i+5, 4+k).setCellFeatures(addColumnList(attrValueArray));
						}
					}else{
						firstSheet.addCell(new Label(i+5, 2, "attr_tagName2_" + tag.getTagId()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��poi api���ĵ�λ��������ݣ�ʹ�������б���Ա༭
	 * @param path
	 */
	private void processFirstSheetForMinOrderUnit(String path){
		POIFSFileSystem fs;
		try {
			fs = new POIFSFileSystem(new FileInputStream(path));
			HSSFWorkbook resourceFile = new HSSFWorkbook(fs);
			
			// ��λ������
			HSSFSheet sheet1 = resourceFile.getSheetAt(0);
			if (sheet1 == null) {
				return;
			}
			HSSFRow row = sheet1.getRow(0);
			int rowNum = sheet1.getLastRowNum();
			int coloumNum=row.getPhysicalNumberOfCells();
			// ���������б�
			// ֻ�ԣ�0��0����Ԫ����Ч
			CellRangeAddressList regions = new CellRangeAddressList(4, rowNum,
					coloumNum - 3, coloumNum - 3);
			// ��������������
			DVConstraint constraint = DVConstraint
					.createExplicitListConstraint(StringUtil.toArray(StringUtil
							.toString(Product.MINORDER_UNIT_LIST, ","), ","));
			// �����������������
			HSSFDataValidation data_validation = new HSSFDataValidation(
					regions, constraint);
			data_validation.setShowErrorBox(false);
			// ��sheetҳ��Ч
			sheet1.addValidationData(data_validation);
			// д���ļ�
			FileOutputStream fileOut = new FileOutputStream(path);
			resourceFile.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��������б�
	 * @param contentArray
	 * @return
	 */
	private WritableCellFeatures addColumnList(String[] contentArray) {
		List<String> contentList = new ArrayList<String>();
		WritableCellFeatures wcf = new WritableCellFeatures();
		for (int i = 0; i < contentArray.length; i++) {
			contentList.add(contentArray[i]);
		}
		wcf.setDataValidationList(contentList);
		contentList.clear();
		contentList = null;
		return wcf;
	}
	
	/**
	 * �õ�����1�д��ڣ�����2�����ڵ�ֵ
	 * @param array1
	 * @param array2
	 * @return �ö��ŷָ�
	 */
	private String getDifferArray(int[] array1, int[] array2) {
		if (array1 == null) {
			return "";
		}
		String differArray = "";
		for (int str1 : array1) {
			boolean isContain = false;
			if (array2 != null) {
				for (int str2 : array2) {
					if (str1 == str2) {
						isContain = true;
					}
				}
			}
			if (!isContain) {
				differArray += "," + str1;
			}
		}
		return StringUtil.trimComma(differArray);
	}

	private String getProIdtoString(int[] proId) {
		return StringUtil.toString(StringUtil.toStringArray(proId), ",");
	}

	public void setCompanyDAO(CompanyDAO companyDAO) {
		this.companyDAO = companyDAO;
	}

	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

	public void setNewProductDAO(NewProductDAO newProductDAO) {
		this.newProductDAO = newProductDAO;
	}

	public void setGroupDAO(GroupDAO groupDAO) {
		this.groupDAO = groupDAO;
	}

	public void setSpecialGroupDAO(SpecialGroupDAO specialGroupDAO) {
		this.specialGroupDAO = specialGroupDAO;
	}

	public void setTradeDAO(TradeDAO tradeDAO) {
		this.tradeDAO = tradeDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public void setVideoDAO(VideoDAO videoDAO) {
		this.videoDAO = videoDAO;
	}
}