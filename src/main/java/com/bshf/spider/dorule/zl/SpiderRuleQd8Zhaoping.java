package com.bshf.spider.dorule.zl;

import com.bshf.spider.dorule.abs.SpiderRuleAbstract;
import com.bshf.spider.entity.GoodsPO;
import com.bshf.util.IPModel.IPMessage;
import com.bshf.util.RegexUtil;
import com.bshf.util.database.MyRedis;
import com.bshf.util.httpbrowser.MyHttpResponse;

import java.util.List;

/**
 * 用药参考网
 * http://drugs.medlive.cn/index.jsp
 * 根据关键字 取得链接 并取得其内容
 * */
public class SpiderRuleQd8Zhaoping extends SpiderRuleAbstract {
	@Override
	public void runSpider(GoodsPO goodsPO)  {
		try {

			// 设置代理IP
			MyRedis redis = new MyRedis();
			//从redis数据库中随机拿出一个IP
			IPMessage ipMessage = redis.getIPByList();
			redis.close();
			String htmlSource = MyHttpResponse.getHtml(goodsPO.getWebUrl(), ipMessage.getIPAddress(), ipMessage.getIPPort());



			//	1-2 输出查看效果
			//System.out.println("htmlSource=============="+htmlSource);

			//	2-1 提取有效内容
			// <a href="http://beijing.qd8.com.cn/yewu/xinxi12_2804280.html" target="_blank" class="list_title">
			String rgex = "<a href=\"(.*?)\" target=\"_blank\" class=\"list_title\">";
			List<String> stringList = RegexUtil.getSubUtil(htmlSource, rgex);
			//System.out.println(stringList.toString());
			for (String mystr : stringList) {
				goodsPO.setName(mystr);
				goodsPO.setType(ipMessage.getIPAddress()+":"+ipMessage.getIPPort());
				GoodsPO savePO = new GoodsPO();
				savePO.setWebUrl(mystr);
				savePO.setType(ipMessage.getIPAddress());
				savePO.setName(ipMessage.getIPPort());

				SpiderRuleQd8ZhaopingDetail spiderRuleQd8ZhaopingDetail = new SpiderRuleQd8ZhaopingDetail();
				spiderRuleQd8ZhaopingDetail.runSpider(savePO);
			}


		}catch (Exception e){
			//不处理异常
		}

	}



	public static void main(String[] args) {
		GoodsPO goodsPO = new GoodsPO();
		String srcUrl = "http://beijing.qd8.com.cn/yewu/p1/";
		goodsPO.setWebUrl(srcUrl);
		goodsPO.setOrderNum("1");
		SpiderRuleQd8Zhaoping spiderUtils = new SpiderRuleQd8Zhaoping();
		spiderUtils.runSpider(goodsPO);
	}
}

