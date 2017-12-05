package sinoway;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import utils.MailUtils;

public class MailTest {
	public static void main(String[] args) {
		MailUtils.setBase("smtp.mxhichina.com", "xxxxx@xxxx.com", "xxxxxxx");
		MailUtils.setAddress("xxxx@xxx.com", "xxxx@xxx.com", "测试");
		MailUtils.setContent("测试数据");//普通文本
		//MailUtils.setContent("<div><ol><li>1</li><li>1</li><li>1</li><li>1</li></ol></div>");//hmtl 格式
		MailUtils.send(true);
	}
}

