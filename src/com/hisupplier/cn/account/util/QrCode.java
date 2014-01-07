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
	 * ���ɶ�ά��(QRCode)ͼƬ
	 * 
	 * @param content ��ά��ͼƬ������
	 * @param imgPath ���ɶ�ά��ͼƬ������·��
	 * @return 0:��ά�����ɳɹ�,-1:�ַ����ȳ�����Χ,-100:��ά������ʧ��
	 */
	public int createQRCode(String content, String imgPath) {
		try {
			Qrcode qrcodeHandler = new Qrcode();
			// ���ö�ά���Ŵ��ʣ���ѡL(7%)��M(15%)��Q(25%)��H(30%)���Ŵ���Խ�߿ɴ洢����ϢԽ�٣����Զ�ά�������ȵ�Ҫ��ԽС
			qrcodeHandler.setQrcodeErrorCorrect('M');
			// N��������,A�����ַ�a-Z,B���������ַ�
			qrcodeHandler.setQrcodeEncodeMode('B');
			// �������ö�ά��汾��ȡֵ��Χ1-40��ֵԽ��ߴ�Խ�󣬿ɴ洢����ϢԽ��
			qrcodeHandler.setQrcodeVersion(3);
			// ͼƬ�ߴ�
			// int imgSize = 67 + 12 * (size - 1);
			byte[] contentBytes = content.getBytes("utf-8");
			BufferedImage bufImg = new BufferedImage(91, 91, BufferedImage.TYPE_INT_RGB);
			Graphics2D gs = bufImg.createGraphics();
			// ���ñ�����ɫ
			gs.setBackground(Color.WHITE);
			gs.clearRect(0, 0, 91, 91);

			// �趨ͼ����ɫ > BLACK
			gs.setColor(Color.BLACK);

			// ����ƫ���� �����ÿ��ܵ��½�������
			int pixoff = 2;
			// ������� > ��ά��
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
			// ���ɶ�ά��QRCodeͼƬ
			File imgFile = new File(imgPath);
			ImageIO.write(bufImg, "jpg", imgFile);

		} catch (Exception e) {
			log.error(e);
			return -100;
		}
		return 0;
	}
}
