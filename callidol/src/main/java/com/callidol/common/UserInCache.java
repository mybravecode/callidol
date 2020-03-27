package com.callidol.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import com.callidol.utils.HashUtil;
import com.callidol.utils.RedisOp;

@Component
public class UserInCache {
	@Value("${spring.service.addr}")
	private String addr;

	@Autowired
	private RedisOp redisOp;

	@Autowired
	private StringRedisTemplate redisTemplate;

	public final static String LastGetFreeChanceTime = "LastGetFreeChanceTime";

	public final static String LastClickShareTime = "LastClickShareTime";

	public final static String RestCallChance = "RestChance";

	public final static String ShareUrl = "ShareUrl";

	public final static String callForIdolLuaScript = "local restChance = redis.call('HINCRBY', KEYS[1], KEYS[2], 0);"// 增加0个
			+ "if restChance == 0 then " + "return -200;" + "end;" + "local callNum = tonumber(ARGV[1]);"// tonumber :
																											// 将String类型转化为int
			+ "if restChance < callNum then " + "return -restChance;" // 如果剩余次数不够，返回-restChance
			+ "else " // 否则 返回减去callNum的执行结果
			+ "return redis.call('HINCRBY', KEYS[1], KEYS[2], -callNum);" + "end";

	// 根据用户id 得到第一个key:"user-result" + userId
	private static String getUserMapName(long userId) {
		return "userId-" + userId + "-info";
	}

	// ---------------------RestCallChance 当前剩余打榜次数
	// 当前剩余打榜次数
	public int getRestCallChance(long userId) {
		return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, 0);
		// 第一个key:"user-result" + userId 第二个key:RestCallChance 第二个key对应的值：value + 0
	}

	// 增加 当前剩余打榜次数
	public int addRestCallChance(long userId, int addDelta) {
		return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, addDelta);
	}

	// 减少 当前剩余打榜次数
	public int descRestCallChance(long userId, int addDelta) {
		return (int) redisOp.hIncrement(getUserMapName(userId), RestCallChance, -addDelta);
	}

	// ---------------------LastGetFreeChanceTime 获得最后一次免费打榜的时间
	public long getLastGetFreeChanceTime(long userId) { // 毫秒
		return redisOp.hIncrement(getUserMapName(userId), LastGetFreeChanceTime, 0);
	}

	public void setLastGetFreeChanceTime(long userId, long currentTime) {
		redisOp.hSet(getUserMapName(userId), LastGetFreeChanceTime, currentTime);
	}

	// ---------------------ShareUrl 获取某个用户的专属分享链接
	public String genShareUrl(long userId) {
		String shareCode = HashUtil.hash(userId + "callidol123456789");
		return "<href>" + addr + "/apiv1/user/clickShare?code=" + shareCode + "</href>";
	}

	// ---------------------LastClickShareTime 最后一次 点击 其他用户分享链接的时间
	public long getLastClickShareTime(long userId) { // 毫秒
		return redisOp.hIncrement(getUserMapName(userId), LastClickShareTime, 0);
	}

	public void setLastClickShareTime(long userId, long currentTime) {
		redisOp.hSet(getUserMapName(userId), LastClickShareTime, currentTime);
	}

	// ------------ 减去打榜次数并返回剩余次数，这里使用lua脚本保证多条命令的原子性和执行顺序---------
	// 查询用户剩余打榜次数，如果比打榜数小，则返回剩余打榜次数，否则减去剩余打榜次数并返回减去后的值
	public int queryAndSubRestChanceByCallNum(long userId, Integer callNum) {
		// 改成lua脚本的方式，将多条命令合并为一条,查询用户剩余打榜次数并返回
		DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
		redisScript.setScriptText(callForIdolLuaScript);
		redisScript.setResultType(Long.class);
		List<String> keys = new ArrayList<>();

		keys.add(getUserMapName(userId));
		keys.add(RestCallChance);

		long restChance = redisTemplate.execute(redisScript, keys, callNum.toString());
		
		//压力测试的时候使用
		addRestCallChance(userId, callNum);
		
		return (int) restChance;
	}

}
