/*
package com.boot.nio.signal;

//import com.wiscom.signal.signalserver.les.LesCacheUtil;
//import com.wiscom.signal.signalserver.les.push.CrossControlModeMsg;
//import com.wiscom.signal.signalserver.les.push.CrossCycleMsg;
//import com.wiscom.signal.signalserver.les.push.CrossStageMsg;
//import com.wiscom.signal.signalserver.les.request.*;
//import com.wiscom.signal.signalserver.les.response.*;
import lombok.extern.log4j.Log4j2;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class LesProtocolCoder extends CumulativeProtocolDecoder implements ProtocolEncoder{
	private byte[] head = new byte[4];

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(1000).setAutoExpand(true);
		for (int i = 0; i < LesCacheUtil.head.length; ++i) {
			buffer.put(LesCacheUtil.head[i]);
		}
		if (message instanceof LoginReply) {
			LoginReply reply = (LoginReply)message;
			buffer.put(reply.getTabNo());
			buffer.put(reply.getTabLen());
			buffer.put(reply.getRegionNo());
			buffer.put(reply.getAckRe());
			buffer.put(reply.getTSCId());
			buffer.put(reply.getReserve());
		}
		else if (message instanceof CrossInfoReq) {
			CrossInfoReq crossInfoReq = (CrossInfoReq)message;
			buffer.put(crossInfoReq.getTabNo());
			buffer.put(crossInfoReq.getTabLen());
			buffer.put(crossInfoReq.getRegionNo());
			buffer.put(crossInfoReq.getMsgType());
			buffer.put(crossInfoReq.getSubRegionNo());
			buffer.put(crossInfoReq.getCrossId());
		} else if(message instanceof CrossPhaseLockReq) {
			CrossPhaseLockReq phaseLockReq = (CrossPhaseLockReq)message;
			buffer.put(phaseLockReq.getTabNo());
			buffer.put(phaseLockReq.getTabLen());
			buffer.put(phaseLockReq.getRegionNo());
			buffer.put(phaseLockReq.getCrossId());
			buffer.put(phaseLockReq.getPhaseNo());
			buffer.put(phaseLockReq.getPhaseLen());
		} else if(message instanceof CrossControlModeLockReq) {
			CrossControlModeLockReq controlModeReq = (CrossControlModeLockReq)message;
			buffer.put(controlModeReq.getTabNo());
			buffer.put(controlModeReq.getTabLen());
			buffer.put(controlModeReq.getRegionNo());
			buffer.put(controlModeReq.getCrossId());
			buffer.put(controlModeReq.getMode());
		}
		else if(message instanceof CrossSetPeriodReq) {
			CrossSetPeriodReq periodSetReq = (CrossSetPeriodReq)message;
			buffer.put(periodSetReq.getTabNo());
			buffer.put(periodSetReq.getTabLen());
			buffer.put(periodSetReq.getRegionId());
			buffer.put(periodSetReq.getCrossId());
			buffer.put(periodSetReq.getLoadNo());
			for(CrossSetPeriod period : periodSetReq.getPeriods()) {
				buffer.put(period.getHour());
				buffer.put(period.getMinute());
				buffer.put(period.getMode());
				buffer.put(period.getPlanId());
			}
		}
		else if(message instanceof CrossSetPlanOffSetReq) {
			CrossSetPlanOffSetReq planOffsetSetReq = (CrossSetPlanOffSetReq)message;
			buffer.put(planOffsetSetReq.getTabNo());
			buffer.put(planOffsetSetReq.getTabLen());
			buffer.put(planOffsetSetReq.getRegionId());
			buffer.put(planOffsetSetReq.getCrossId());
			buffer.put(planOffsetSetReq.getLoadNo());
			buffer.put(planOffsetSetReq.getPlanNo());
			buffer.put(planOffsetSetReq.getOffset());
			buffer.put(planOffsetSetReq.getStage());
		}
		else if(message instanceof CrossSetPlanReq) {
			CrossSetPlanReq planSetReq = (CrossSetPlanReq)message;
			buffer.put(planSetReq.getTabNo());
			buffer.put(planSetReq.getTabLen());
			buffer.put(planSetReq.getRegionId());
			buffer.put(planSetReq.getCrossId());
			buffer.put(planSetReq.getLoadNo());
			buffer.put(planSetReq.getPlanNo());
			buffer.put(planSetReq.getStageNum());
			for(byte stageNo : planSetReq.getStageNoList()) {
				buffer.put(stageNo);
			}

			for(int i = 0; i < 16 -planSetReq.getStageNoList().size(); i++) {
				buffer.put((byte)0);
			}
			for(byte stageLen : planSetReq.getStageLenList()) {
				buffer.put(stageLen);
			}
			for(int i = 0; i < 16 - planSetReq.getStageLenList().size(); i++) {
				buffer.put((byte)0);
			}
		}
		else {
			buffer.put((byte[])message);
		}
		buffer.flip();
		out.write(buffer);
	}

	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		//		printMessage(in);
		Byte tabNo = 0;
		Byte tabLen = 0;
		boolean isMissed = false;
		in.mark();
		int i = 0;
		while (i < 4) {
			head[i] = in.get();
			//			log.info("head" + i + "," + head[i]);
			if (head[i] != 22) {
				if (i == 0) {
					removeMessedMsg(in);
					return true;
				}
				if (i == 1) {
					log.info("仅有一个同步字符，不识别为正常消息");
					return true;
				}
				log.info("同步字符缺失");
				tabNo = head[i];
				tabLen = in.get();
				isMissed = true;
				break;
			}
			else {
				++i;
			}
		}
		int remain = in.remaining();
		if (remain <= 2) {
			in.reset();
			return false;
		}
		if (!isMissed) {
			tabNo = in.get();
			tabLen = in.get();
		}
		remain = in.remaining();
		if (tabLen - 2 > remain) {
			in.reset();
			return false;
		}
		try {
			Object message = decodeMsg(tabNo, tabLen, in, out);
			if (message == null) {
				in.reset();
				log.info("remain:" + in.remaining());
				log.info("数据解析失败：" + tabNo + ",重新解析");
				return false;
			}
			out.write(message);
		}
		catch (Exception e) {
			log.error("error:" + e, (Throwable)e);
		}
		return true;
	}

	private void removeMessedMsg(IoBuffer in) {
		log.info("数据包混乱---");
		String message = "messed message:";
		byte messed;
		do {
			messed = in.get();
			message = String.valueOf(message) + messed + ",";
		} while (22 != messed);
		log.info(message);
	}

	protected void printMessage(IoBuffer in) {
		int totalLen = in.remaining();
		in.mark();
		String message = "total:" + totalLen + "message:" + in.get() + ",";
		if (totalLen > 1000) {
			totalLen = 1000;
		}
		for (int i = 1; i < totalLen - 1; ++i) {
			message = String.valueOf(message) + in.get() + ",";
		}
		message = String.valueOf(message) + in.get();
		log.info(message);
		in.reset();
	}

	public long dwordBytesToLong(byte[] data) {
		return (0xFF & data[3]) << 24 | (0xFF & data[2]) << 16 | (0xFF & data[1]) << 8 | (0xFF & data[0]);
	}

	public short wordBytesToshort(byte[] data) {
		return (short)((0xFF & data[1]) << 8 | (0xFF & data[0]));
	}

	public byte[] longToDword(long value) {
		final byte[] data = new byte[4];
		for (int i = 0; i < data.length; ++i) {
			data[i] = (byte)(value >> 8 * i);
		}
		return data;
	}

	public byte[] shortToword(Short value) {
		final byte[] data = new byte[2];
		for (int i = 0; i < data.length; ++i) {
			data[i] = (byte)(value >> 8 * i);
		}
		return data;
	}

	private Object decodeMsg(byte tabNo, byte tabLen, IoBuffer in, ProtocolDecoderOutput out) {
		Object message;
		if (tabNo == 101) {
			message = decodeLoginMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 2) {
			message = decodeControlMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 3) {
			message = decodeCycleMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 4) {
			message = decodePhaseMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 30) {
			message = decodeFaultMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 45) {
			log.info("接收到方案配置信息");
			message = decodePeriodMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 33) {
			log.info("接收到路口基本信息");
			message = decodeCrossBasicMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 47) {
			log.info("接收到星期方案基本信息");
			message = decodeWeekPlanMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 44) {
			log.info("接收到方案下发返回信息");
			message = decodePlanSetReplyMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 46) {
			log.info("接收到过渡灯色配置信息");
			message = decodeLampConfMsg(tabNo, tabLen, in);
		}
		else if (tabNo == 20) {
			log.info("下发命令响应");
			message = decodeControlReplyMsg(tabNo, tabLen, in);
		}
		else {
			log.info("未识别命令类型：" + tabNo);
			int length = 0;
			length = (0xFF & tabLen);
			String msg = "message-unrecognized:" + tabNo + "," + length + ",";
			for (int i = 0; i < length - 2; ++i) {
				msg = String.valueOf(msg) + in.get() + ",";
			}
			log.info(msg);
			return msg;
		}
		return message;
	}

	private CrossControlReply decodeControlReplyMsg(byte tabNo, byte tabLen, IoBuffer in) {
		CrossControlReply controlReply = new CrossControlReply();
		controlReply.setTabNo(tabNo);
		controlReply.setTabLen(tabLen);
		controlReply.setRegionNo(in.get());
		controlReply.setCrossId(getCrossId(in));
		controlReply.setType(in.get());
		controlReply.setIden(in.get());
		controlReply.setAck(in.get());
		String leftMsg = "left-2:";
		log.info("controlReply:" + controlReply);
		if (tabLen - 7 > 0) {
			for (int i = 0; i < tabLen - 7; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info(leftMsg);
		}
		return controlReply;
	}

	private LoginReq decodeLoginMsg(byte tabNo, byte tabLen, IoBuffer in) {
		LoginReq loginReq = new LoginReq();
		loginReq.setTabNo(tabNo);
		loginReq.setTableLen(tabLen);
		loginReq.setRegionId(in.get());
		loginReq.setReg(in.get());
		loginReq.setTSCId(in.get());
		loginReq.setReserved(in.get());
		byte[] identifier = new byte[40];
		int left = 0;
		int realLen;
		if (tabLen - 6 > 40) {
			realLen = 40;
			left = tabLen - 6 - 40;
		}
		else {
			realLen = tabLen - 6;
		}
		for (int j = 0; j < realLen; ++j) {
			identifier[j] = in.get();
		}
		loginReq.setIdentifier(identifier);
		log.info("logining---" + loginReq);
		if (left > 0) {
			String leftMsg = "leftMsg-101:";
			for (int i = 0; i < left; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get();
			}
			log.info(leftMsg);
		}
		return loginReq;
	}

	private CrossControlModeMsg decodeControlMsg(byte tabNo, byte tabLen, IoBuffer in) {
		CrossControlModeMsg controleMode = new CrossControlModeMsg();
		controleMode.setTabNo(tabNo);
		controleMode.setTabLen(tabLen);
		controleMode.setRegionNo(in.get());
		controleMode.setCrossId(getCrossId(in));
		controleMode.setControlMode(in.get());
		controleMode.setFeature(in.get());
		String leftMsg = "left-2:";
		log.info("controlmode:" + controleMode);
		if (tabLen - 6 > 0) {
			for (int i = 0; i < tabLen - 6; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info(leftMsg);
		}
		return controleMode;
	}

	private CrossPlanSetReply decodePlanSetReplyMsg(byte tabNo, byte tabLen, IoBuffer in) {
		CrossPlanSetReply planSetReply = new CrossPlanSetReply();
		planSetReply.setTabNo(tabNo);
		planSetReply.setTabLen(tabLen);
		planSetReply.setRegionNo(in.get());
		planSetReply.setCrossId(getCrossId(in));
		planSetReply.setLoadNo(in.get());
		planSetReply.setPlanNo(in.get());
		planSetReply.setResult(in.get());

		String leftMsg = "left-44:";
		log.info("planSetReply:" + planSetReply);
		if (tabLen - 7 > 0) {
			for (int i = 0; i < tabLen - 7; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info(leftMsg);
		}
		return planSetReply;
	}

	private CrossCycleMsg decodeCycleMsg(byte tabNo, byte tabLen, IoBuffer in) {
		CrossCycleMsg cycleMsg = new CrossCycleMsg();
		cycleMsg.setTabNo(tabNo);
		cycleMsg.setTabLen(tabLen);
		cycleMsg.setRegionNo(in.get());
		cycleMsg.setCrossId(getCrossId(in));
		byte[] cycleLen = { in.get(), in.get() };
		cycleMsg.setLastCycleLen(wordBytesToshort(cycleLen));
		in.get();
		in.get();
		byte[] startLong = new byte[4];
		for (int i = 0; i < 4; ++i) {
			startLong[i] = in.get();
		}
		cycleMsg.setStartTime(dwordBytesToLong(startLong));
		log.info("cycleMessage:" + cycleMsg);
		String leftMsg = "left-3:";
		if (tabLen - 12 > 0) {
			for (int j = 0; j < tabLen - 12; ++j) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info(leftMsg);
		}
		return cycleMsg;
	}

	private CrossStageMsg decodePhaseMsg(byte tabNo, byte tabLen, IoBuffer in) {
		CrossStageMsg stageMsg = new CrossStageMsg();
		stageMsg.setTabNo(tabNo);
		stageMsg.setTabLen(tabLen);
		stageMsg.setRegionNo(in.get());
		stageMsg.setCrossId(getCrossId(in));
		stageMsg.setLastStageNo(in.get());
		stageMsg.setLastStageLen(in.get());
		stageMsg.setCurStageNo(in.get());
		stageMsg.setCurStageLen(in.get());
		byte[] stageName = new byte[40];
		int left = 0;
		int nameLen;
		if (tabLen - 8 > 40) {
			nameLen = 40;
			left = tabLen - 8 - 40;
		}
		else {
			nameLen = tabLen - 8;
		}
		for (int j = 0; j < nameLen; ++j) {
			stageName[j] = in.get();
		}
		stageMsg.setCurStageName(stageName);
		log.info("stageInfo:" + stageMsg);
		if (left > 0) {
			String leftMsg = "leftMsg-4:";
			for (int i = 0; i < left; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get();
			}
			log.info(leftMsg);
		}
		return stageMsg;
	}

	private QueryFaultReply decodeFaultMsg(byte tabNo, byte tabLen, IoBuffer in) {
		QueryFaultReply queryFault = new QueryFaultReply();
		queryFault.setTabNum(tabNo);
		queryFault.setTabLength(tabLen);
		queryFault.setRegionId(in.get());
		queryFault.setMsgType(in.get());
		queryFault.setSubRegionId(in.get());
		queryFault.setCrossId(getCrossId(in));
		queryFault.setCode(in.get());
		log.info("查询失败返回：" + queryFault);
		if (tabLen - 7 > 0) {
			String leftMsg = "leftMsg-30:";
			for (int i = 0; i < tabLen - 7; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get();
			}
			log.info(leftMsg);
		}
		return queryFault;
	}

	private CrossBasicInfoReply decodeCrossBasicMsg(byte tabNo, byte tabLen, IoBuffer in) {
		byte[] length = { tabLen, in.get() };

		short tabLength = wordBytesToshort(length);
		CrossBasicInfoReply crossBasicInfo = new CrossBasicInfoReply();
		crossBasicInfo.setTabNo(tabNo);
		crossBasicInfo.setLowTabLen(length[0]);
		crossBasicInfo.setHighTabLen(length[1]);

		crossBasicInfo.setTabLen(tabLength);
		if (tabLength - 3 > in.remaining()) {
			return null;
		}
		crossBasicInfo.setRegionNo(in.get());
		byte crossNum = in.get();
		crossBasicInfo.setCrossNum(crossNum);

		int left = tabLength - 5;
		List<Integer> crossIdList = new ArrayList<>();
		for (int i = 0; i < crossNum; ++i) {
			crossIdList.add(getCrossId(in));
		}
		crossBasicInfo.setCrossIdList(crossIdList);
		List<String> crossNameList = new ArrayList<>();
		List<Byte> useLessList = new ArrayList<>();
		for(int i = 0; i<(250 - crossNum); i++) {
			useLessList.add(in.get());
		}
//		log.info("路口编号与名称中间过滤无效字符："+useLessList);
		left = left - 250;
		for(int i = 0; i < crossNum; ++i) {
			byte[] crossName = new byte[40];
			List<Byte> crossNameBytes = new ArrayList<>();
			for(int k = 0; k < 40; ++k) {
				crossName[k] = in.get();
				crossNameBytes.add(crossName[k]);
			}
			crossNameList.add(new String(crossName).trim());
		}
		left = left - crossNum*40;
		crossBasicInfo.setCrossNameList(crossNameList);
		log.info("crossBasicInfo:" + crossBasicInfo);
		if (left > 0) {
			String leftMsg = "leftMsg-33:";
			for (int i = 0; i < left; i++) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info("left:" + left + ";" + leftMsg.length());
		}
		return crossBasicInfo;
	}

	private CrossWeekPlanInfoReply decodeWeekPlanMsg(byte tabNo, byte tabLen, IoBuffer in) {
		byte[] length = { tabLen, in.get() };

		short tabLength = wordBytesToshort(length);
		CrossWeekPlanInfoReply weekPlanReply = new CrossWeekPlanInfoReply();

		weekPlanReply.setTabNo(tabNo);
		weekPlanReply.setLowTabLen(length[0]);
		weekPlanReply.setHighTabLen(length[1]);
		weekPlanReply.setTabLen(tabLength);
		if (tabLength - 3 > in.remaining()) {
			return null;
		}
		weekPlanReply.setRegionNo(in.get());
		weekPlanReply.setJuncId(getCrossId(in));
		weekPlanReply.setWeekNo(in.get());
		weekPlanReply.setIsExecute(in.get());
		byte periodNum = in.get();
		weekPlanReply.setPeriodNum(periodNum);
		int left = tabLength - 8;
		in.get();
		in.get();
		for (int i = 0; i < periodNum; ++i) {
			CrossWeekPeriodInfo weekPeriod = new CrossWeekPeriodInfo();
			byte[] cycleLen = { in.get(), in.get() };
			weekPeriod.setCycleLen(wordBytesToshort(cycleLen));
			byte[] offset = { in.get(), in.get() };
			weekPeriod.setOffset(wordBytesToshort(offset));
			weekPeriod.setOffStage(in.get());
			byte stageNum = in.get();
			weekPeriod.setStageNum(stageNum);
			List<Byte> stages = new ArrayList<>();
			for (int j = 0; j < 16; ++j) {
				if (j < stageNum) {
					stages.add(in.get());
				}
				else {
					in.get();
				}
			}
			weekPeriod.setStageList(stages);
			List<Byte> stageLens = new ArrayList<>();
			for (int k = 0; k < 16; ++k) {
				if (k < stageNum) {
					stageLens.add(in.get()) ;
				}
				else {
					in.get();
				}
			}
			weekPeriod.setStageLenList(stageLens);
			weekPeriod.setPlanId(i + 1);
			in.get();
			in.get();
			byte[] startTime = new byte[4];
			for (int l = 0; l < 4; ++l) {
				startTime[l] = in.get();
			}
			weekPeriod.setStartTime(dwordBytesToLong(startTime));
			byte[] endTime = new byte[4];
			for (int m = 0; m < 4; ++m) {
				endTime[m] = in.get();
			}
			weekPeriod.setEndTime(dwordBytesToLong(endTime));
			weekPeriod.setIdDir(in.get());
			in.get();
			in.get();
			in.get();
			left -= 52;
			weekPlanReply.getPeriodPlanList().add(weekPeriod);
		}
		weekPlanReply.sortPeriod();
		if (left > 0) {
			String leftMsg = "leftMsg-47:";
			for (int i2 = 0; i2 < left; i2++) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info("left:" + left + ";" + leftMsg);
		}
		log.info("weekPlanReply:" + weekPlanReply);
		return weekPlanReply;
	}

	private PeriodPlanReply decodePeriodMsg(byte tabNo, byte tabLen, IoBuffer in) {
		byte[] length = { tabLen, in.get() };

		short tabLength = wordBytesToshort(length);
		PeriodPlanReply periodPlanReply = new PeriodPlanReply();
		periodPlanReply.setTabNo(tabNo);
		periodPlanReply.setLowTabLen(length[0]);
		periodPlanReply.setHighTabLen(length[1]);
		periodPlanReply.setTabLen(tabLength);
		if (tabLength - 3 > in.remaining()) {
			return null;
		}
		periodPlanReply.setRegionNo(in.get());
		periodPlanReply.setCrossId(getCrossId(in));
		byte periodNum = in.get();
		periodPlanReply.setPeriodNum(periodNum);
		in.get();
		in.get();
		int left = tabLength - 8;
		for (int i = 0; i < periodNum; ++i) {
			PeriodInfoReply period = new PeriodInfoReply();
			byte[] cycleLen = { in.get(), in.get() };
			period.setCycleLen(wordBytesToshort(cycleLen));
			byte[] offset = { in.get(), in.get() };
			period.setOffset(wordBytesToshort(offset));
			period.setOffStage(in.get());
			byte stageNum = in.get();
			period.setStageNum(stageNum);
			List<Byte> stages = new ArrayList<>();
			for (int j = 0; j < 16; ++j) {
				if (j < stageNum) {
					stages.add(in.get());
				}
				else {
					in.get();
				}
			}
			period.setStages(stages);
			List<Byte> stageLens = new ArrayList<>();
			for (int k = 0; k < 16; ++k) {
				if (k < stageNum) {
					stageLens.add(in.get());
				}
				else {
					in.get();
				}
			}
			period.setStageLens(stageLens);
			period.setPlanId(i + 1);
			in.get();
			in.get();
			byte[] startTime = new byte[4];
			for (int l = 0; l < 4; ++l) {
				startTime[l] = in.get();
			}
			period.setStartTime(dwordBytesToLong(startTime));
			byte[] endTime = new byte[4];
			for (int m = 0; m < 4; ++m) {
				endTime[m] = in.get();
			}
			period.setEndTime(dwordBytesToLong(endTime));
			period.setIdDir(in.get());
			in.get();
			in.get();
			in.get();
			left -= 52;
			periodPlanReply.getPeriods().add(period);
		}
		periodPlanReply.sortPeriod();
		if (left > 0) {
			String leftMsg = "leftMsg-34:";
			for (int i2 = 0; i2 < left; i2++) {
				leftMsg = String.valueOf(leftMsg) + in.get() + ",";
			}
			log.info("left:" + left + ";" + leftMsg);
		}
		log.info("plan:" + periodPlanReply);
		return periodPlanReply;
	}

	private LampConfReply decodeLampConfMsg(byte tabNo, byte tabLen, IoBuffer in) {
		LampConfReply lampConfReply = new LampConfReply();
		lampConfReply.setTabNo(tabNo);
		lampConfReply.setTabLen(tabLen);
		lampConfReply.setRegionNo(in.get());
		lampConfReply.setCrossId(getCrossId(in));
		for(int i=0; i<16; i++) {
			LampConf lampConf = new LampConf();
			lampConf.setYellow(in.get());
			lampConf.setClearRed(in.get());
			lampConf.setPedGreenFlash(in.get());
			lampConf.setVihGreenFlash(in.get());
			lampConf.setPedRedFlash(in.get());
			lampConf.setVihRedFlash(in.get());
			lampConfReply.getLampConfList().add(lampConf);
		}

		if (tabLen - 100 > 0) {
			String leftMsg = "leftMsg-46:";
			for (int i = 0; i < tabLen - 10; ++i) {
				leftMsg = String.valueOf(leftMsg) + in.get();
			}
			log.info(leftMsg);
		}
		return lampConfReply;
	}

	private int getCrossId(IoBuffer in) {
		byte crossId = in.get();
		if (crossId < 0) {
			int crossID = 0xFF & crossId;
			return crossID;
		}
		return crossId;
	}

	public static void main(String[] args) {
		byte a = -52;
		System.err.println(0xFF & a);
	}
}
*/
