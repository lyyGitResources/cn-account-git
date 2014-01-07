package com.hisupplier.cn.account.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.swetake.util.Qrcode;

public class QrCode {

	private static Log log = LogFactory.getLog(QrCode.class);

	/**
	 * 生成二维码(QRCode)图片
	 * 
	 * @param content 二维码图片的内容
	 * @param imgPath 生成二维码图片完整的路径
	 * @return 0:二维码生成成功,-1:字符长度超出范围,-100:二维码生成失败
	 */
	public int createQRCode(String content, String imgPath) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// 设置二维码排错率，可选L(7%)、M(15%)、Q(25%)、H(30%)，排错率越高可存储的信息越少，但对二维码清晰度的要求越小
			qrcodeHandler.setQrcodeErrorCorrect('M');
			// N代表数字,A代表字符a-Z,B代表其他字符
			qrcodeHandler.setQrcodeEncodeMode('B');
			// 设置设置二维码版本，取值范围1-40，值越大尺寸越大，可存储的信息越大
			qrcodeHandler.setQrcodeVersion(3);
			// 图片尺寸
			// int imgSize = 67 + 12 * (size - 1);
			byte[] contentBytes = content.getBytes("utf-8");
			BufferedImage bufImg = new BufferedImage(91, 91, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// 设置背景颜色
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 91, 91);

			// 设定图像颜色 > BLACK
			gs.setColor(Color.BLACK);

			// 设置偏移量 不设置可能导致解析出错
			int pixoff = 2;
			// 输出内容 > 二维码
			if (contentBytes.length > 0 && contentBytes.length < 300) {
				boolean[][] codeOut = qrcodeHandler.calQrcode(contentBytes);
				for (int i = 0; i < codeOut.length; i++) {
					for (int j = 0; j < codeOut.length; j++) {
						if (codeOut[j][i]) {
							gs.fillRect(j * 3 + pixoff, i * 3 + pixoff, 3, 3);
						}
					}
				}
			} else {
				log.error("QRCode content bytes length = " + contentBytes.length + " not in [ 0,125]. ");
				return -1;
			}
			gs.dispose();
			bufImg.flush();
			// 生成二维码QRCode图片
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "jpg", imgFile);

		} catch (Exception e) {
			log.error(e);
			return -100;
		}
		return 0;
	}
}
