/*
package com.wiscom.signal.signalserver.les.server;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.session.IoSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wiscom.signal.bean.CommonSignalResponseBean;
import com.wiscom.signal.bean.PspTrSigCtrlDayPlanBean;
import com.wiscom.signal.bean.PspTrSigCtrlDayplanPeriodBean;
import com.wiscom.signal.bean.PspTrSigCtrlDispatchPlanBean;
import com.wiscom.signal.bean.PspTrSigCtrlPhaseBean;
import com.wiscom.signal.bean.PspTrSigCtrlPlanBean;
import com.wiscom.signal.bean.PspTrSigCtrlSigdeviceBean;
import com.wiscom.signal.bean.PspTrSigCtrlStageBean;
import com.wiscom.signal.bean.PspTrSignalCrossBean;
import com.wiscom.signal.dao.ISignalDao;
import com.wiscom.signal.job.SignalHisRecordJob;
import com.wiscom.signal.job.SignalMessageJob;
import com.wiscom.signal.model.SignalPhaseModel;
import com.wiscom.signal.model.SyncLatchModel;
import com.wiscom.signal.signalserver.huatong.HuaTongCacheUtil;
import com.wiscom.signal.signalserver.les.LesCacheUtil;
import com.wiscom.signal.signalserver.les.push.CrossControlModeMsg;
import com.wiscom.signal.signalserver.les.push.CrossCycleMsg;
import com.wiscom.signal.signalserver.les.push.CrossStageMsg;
import com.wiscom.signal.signalserver.les.request.CrossInfoReq;
import com.wiscom.signal.signalserver.les.request.CrossSetPeriodReq;
import com.wiscom.signal.signalserver.les.request.CrossSetPlanOffSetReq;
import com.wiscom.signal.signalserver.les.request.CrossSetPlanReq;
import com.wiscom.signal.signalserver.les.request.LoginReq;
import com.wiscom.signal.signalserver.les.response.CrossBasicInfoReply;
import com.wiscom.signal.signalserver.les.response.CrossControlReply;
import com.wiscom.signal.signalserver.les.response.CrossPlanSetReply;
import com.wiscom.signal.signalserver.les.response.LampConfReply;
import com.wiscom.signal.signalserver.les.response.LoginReply;
import com.wiscom.signal.signalserver.les.response.PeriodInfoReply;
import com.wiscom.signal.signalserver.les.response.PeriodPlanReply;
import com.wiscom.signal.signalserver.les.response.QueryFaultReply;
import com.wiscom.signal.signalserver.std1049.response.LaneParam;
import com.wiscom.signal.util.GlobalUtil;
import com.wiscom.signal.util.JsonUtil;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class LesMessageHandler2 {
	@Autowired
	private ISignalDao signalDao;

	private Map<String, String> lastControlModeMap = new HashMap<>();

	public void handleMessage(final IoSession session, final Object message) {
		handle(session, message);
	}

	public void handle(final IoSession session, final Object message) {
		try {
			if (message instanceof LoginReq) {
				LoginReq req = (LoginReq)message;
				LoginReply reply = new LoginReply();
				reply.setRegionNo(req.getRegionId());
				reply.setAckRe((byte)0);
				reply.setTSCId(req.getTSCId());
				reply.setReserve((byte)0);
				reply.setTabLen((byte)6);
				session.write(reply);
				LesCacheUtil.sessionMap.put(String.valueOf(req.getRegionId()), session);
				log.info("区域机登录成功，区域机编号：" + req.getRegionId());

				new Thread(new Runnable() {

					@Override
					public void run() {
						log.info("开始查询区域:" + req.getRegionId()+"下的路口信息");
						requestRegionCross(session, req.getRegionId());
						//		                List<String> crossNos = signalDao.qryAllSignalCrossIDs(GlobalUtil.LES);
						//		                List<String> crossIdList = new ArrayList<>();
						//		                for(String crossNo : crossNos) {
						//		                	crossIdList.add(crossNo.split(GlobalUtil.FACSEPERATOR)[0]);
						//		                }
						//		                log.info("开始更新莱斯信号机数据,更新路口:" + crossIdList);
						//		    			requestCrossInfo(crossIdList);
						//		    			log.info("本次更新莱斯信号机数据结束");
					}


				}).run();
			}
			else if (message instanceof PeriodPlanReply) {
				log.info("收到信号机周期计划数据PeriodPlanReply");
				PeriodPlanReply periodReply = (PeriodPlanReply)message;
				handlePeriodPlanMsg(periodReply);
			}
			else if (message instanceof LampConfReply) {
				log.info("收到信号机灯色配置数据LampConfReply");
				LampConfReply lampReply = (LampConfReply)message;
				handleLampInfo(lampReply);
			}
			else if (message instanceof QueryFaultReply) {
				log.info("收到信号机故障数据QueryFaultReply");
				QueryFaultReply faultReply = (QueryFaultReply)message;
				handleFault(faultReply);
			}
			else if (message instanceof CrossControlReply) {
				log.info("收到信号机控制命令返回数据CrossControlReply");
				CrossControlReply controlReply = (CrossControlReply)message;
				handleControlReply(controlReply);
			}
			else if (message instanceof CrossBasicInfoReply) {
				log.info("收到信号机基本信息数据CrossBasicInfoReply");
				CrossBasicInfoReply crossBasicInfoReply = (CrossBasicInfoReply)message;
				handleCrossBasicInfoReply(crossBasicInfoReply);
			}
			else if(message instanceof CrossPlanSetReply) {
				log.info("收到信号机计划设置返回数据CrossPlanSetReply");
				CrossPlanSetReply planSetReply = (CrossPlanSetReply) message;
				handlePlanSetReply(planSetReply);
			}
			else {
				handleRealMsg(message);
			}
		}
		catch (Exception e) {
			log.error("error--" + e, e);
		}
	}

	public void handleCrossBasicInfoReply(CrossBasicInfoReply crossBasicInfoReply) {
		byte regionId = crossBasicInfoReply.getRegionNo();
		byte crossNum = crossBasicInfoReply.getCrossNum();
		List<PspTrSignalCrossBean> crosses = new ArrayList<>();
		List<PspTrSigCtrlSigdeviceBean> deviceList = new ArrayList<>();
		List<String> crossIdList = new ArrayList<>();
		for(int i = 0; i<crossNum; i++) {
			int crossId = crossBasicInfoReply.getCrossIdList().get(i);
			String crossName = crossBasicInfoReply.getCrossNameList().get(i);
			PspTrSignalCrossBean crossBean = new PspTrSignalCrossBean();
			try {
				String crossNo = regionId+LesCacheUtil.seperator+crossId+GlobalUtil.FACSEPERATOR+GlobalUtil.LES;
				crossBean.setCross_no(crossNo);
				crossBean.setCross_name(crossName.substring(0, crossName.indexOf("\u0000")));
				crossBean.setCross_feature("41");
				crossBean.setCross_state("1");
				crossBean.setIsnetwork(1);
				//    			crossBean.setRegion_no(regionId+GlobalUtil.FACSEPERATOR+GlobalUtil.LES);
				crossBean.setRegion_no("");
				crossBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
				crossBean.setId(UUID.randomUUID().toString().replace("-", ""));
				crossBean.setIskey("0");
				crossBean.setSignalcontroler_id(regionId+LesCacheUtil.seperator+crossId+GlobalUtil.FACSEPERATOR+GlobalUtil.LES);
				crossBean.setSubregion_no("");
				crossBean.setSys_id(GlobalUtil.LES);
				crossBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
				GlobalUtil.crossToNameMap.put(crossNo, crossBean.getCross_name());

				crosses.add(crossBean);
				crossIdList.add(regionId + LesCacheUtil.seperator + crossId);

				PspTrSigCtrlSigdeviceBean deviceBean = new PspTrSigCtrlSigdeviceBean();
				deviceBean.setId(UUID.randomUUID().toString().replace("-", ""));
				deviceBean.setSignalcontroler_id(regionId+LesCacheUtil.seperator+crossId+GlobalUtil.FACSEPERATOR+GlobalUtil.LES);
				deviceBean.setSupplier("Les");
				deviceBean.setSys_id(GlobalUtil.LES);
				deviceBean.setType("signalcontroler");
				deviceBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
				deviceList.add(deviceBean);

				List<PspTrSigCtrlPhaseBean> phaseBeanList = signalDao.qrySignalPhases(crossNo, null);
				if(null == phaseBeanList || phaseBeanList.isEmpty()) {
					log.error("路口" + crossNo + "未配置莱斯基础相位，请立即配置");
				} else {
					for(PspTrSigCtrlPhaseBean phaseBean : phaseBeanList) {
						Map<String, String> phaseToName = GlobalUtil.phaseToNameMap.get(crossNo);
						if(null == phaseToName) {
							phaseToName = new HashMap<>();
						}
						phaseToName.put(phaseBean.getPhase_no(), phaseBean.getPhase_name());
						GlobalUtil.phaseToNameMap.put(crossNo, phaseToName);
					}
				}
			} catch (Exception e) {
				log.error("转换路口：" +regionId+LesCacheUtil.seperator+crossId+ "信息为内部数据失败：" + e, e);
			}
		}
		if(crosses.size() > 0) {
			log.info("保存莱斯路口信息:" + crosses.size());
			signalDao.saveSignalCrosses(crosses, true);
		}
		if(deviceList.size() > 0) {
			log.info("保存莱斯信号机设备信息:" + deviceList.size());
			signalDao.saveSignalDevices(deviceList);
		}
		if(crossIdList.size() > 0) {
			log.info("开始更新莱斯信号机数据,更新路口:" + crossIdList);
			requestCrossInfo(crossIdList);
			log.info("本次更新莱斯信号机数据结束");
		}
	}

	public void requestCrossInfo(List<String> crossIdList) {
		for(String crossID : crossIdList) {
			try {
				String regionNo = crossID.split(LesCacheUtil.seperator)[0];
				IoSession session = LesCacheUtil.sessionMap.get(regionNo);
				if(null == session || session.isClosing()) {
					log.error("区域机：" + regionNo + "尚未登录，无法请求区域机路口信息");
					continue;
				}

				getCrossPeriodPlan(session, crossID);
				getCrossPhaseLamp(session, crossID);
			} catch (Exception e) {
				log.error("通信异常，请检查," + e, e);
			}
		}
		log.info("路口配置请求发送结束，等待异步消息处理");
	}

	private void getCrossPhaseLamp(IoSession session, String crossID) throws Exception {
		CrossInfoReq lampReq = new CrossInfoReq();
		lampReq.setRegionNo(Byte.parseByte(crossID.split(LesCacheUtil.seperator)[0]));
		lampReq.setCrossId(Byte.parseByte(crossID.split(LesCacheUtil.seperator)[1]));
		lampReq.setMsgType(Byte.parseByte(LesCacheUtil.lampReq)); // 10
		//		String lampKey = crossID + LesCacheUtil.seperator + LesCacheUtil.planReq;
		//		SyncLatchModel lampSyncLatch = LesCacheUtil.syncCaches.get(lampKey);
		//		if(null != lampSyncLatch) {
		//			log.warn("区域路口：" + crossID + "重复发送过渡灯色请求");
		//			return;
		//		} else {
		//			lampSyncLatch = new SyncLatchModel();
		//		}
		session.write(lampReq);
		//		try {
		//			LesCacheUtil.syncCaches.put(lampKey, lampSyncLatch);
		//			lampSyncLatch.getLatch().await(60, TimeUnit.SECONDS);
		//			if (null == lampSyncLatch.getMessage()) {
		//				log.error("请求区域路口：" + crossID + "过渡灯色命令响应超时");
		//				return ;
		//			} else {
		//				LesCacheUtil.cacheLampConf.put(crossID, (LampConfReply) lampSyncLatch.getMessage());
		////				return (LampConfReply)lampSyncLatch.getMessage();
		//			}
		//		} finally {
		//			LesCacheUtil.syncCaches.remove(lampKey); // 清理同步参数缓存
		//		}
	}

	private void requestRegionCross(IoSession session,byte regionId) {
		CrossInfoReq crossReq = new CrossInfoReq();
		crossReq.setRegionNo(regionId);
		crossReq.setCrossId((byte)0);
		crossReq.setMsgType(Byte.parseByte(LesCacheUtil.crossReq));
		session.write(crossReq);
	}

	private void getCrossPeriodPlan(IoSession session, String crossID) throws Exception {
		CrossInfoReq planReq = new CrossInfoReq();
		planReq.setRegionNo(Byte.parseByte(crossID.split(LesCacheUtil.seperator)[0]));
		planReq.setCrossId(Byte.parseByte(crossID.split(LesCacheUtil.seperator)[1]));
		planReq.setMsgType(Byte.parseByte(LesCacheUtil.planReq)); // 9
		session.write(planReq);
	}

	private void savePeriodPlanWithLamp(String crossID, PeriodPlanReply periodPlan, LampConfReply lampConf) {
		if(null == periodPlan) {
			log.info(crossID + "时段方案为空，无法保存参数");
			return;
		}
		String cross_no = crossID + GlobalUtil.FACSEPERATOR+GlobalUtil.LES;

		Map<String, Object> curPlanMap = getCurScheduleDayPlan(crossID);
		if(curPlanMap.isEmpty()) {
			log.info("未查到当日调度，不做配置更新");
			return;
		}
		log.info("当前日计划方案信息：" + curPlanMap);
		Map<String, Map<String, String>> plansMap = new HashMap<>(); // 方案一致比对map
		int maxPlanNo = 0; // 初始方案编号
		PspTrSigCtrlDayPlanBean dayPlanBean ;
		List<PspTrSigCtrlDayplanPeriodBean> periodPlanList = null;
		if(curPlanMap.containsKey("dayPlan")) {
			dayPlanBean = (PspTrSigCtrlDayPlanBean) curPlanMap.get("dayPlan");

			Map<String, PspTrSigCtrlPlanBean> planBeanMap = (Map<String, PspTrSigCtrlPlanBean>) curPlanMap.get("planMap");
			Map<String, Map<String, PspTrSigCtrlStageBean>> stageBeanMap =
					(Map<String, Map<String, PspTrSigCtrlStageBean>>) curPlanMap.get("stageMap");
			periodPlanList = (List<PspTrSigCtrlDayplanPeriodBean>) curPlanMap.get("dayPlanPeriodList");

			for(Entry<String, PspTrSigCtrlPlanBean> entry : planBeanMap.entrySet()) {
				String planNo = entry.getKey();
				if(Integer.parseInt(planNo) > maxPlanNo) {
					maxPlanNo = Integer.parseInt(planNo);
				}
				Map<String, PspTrSigCtrlStageBean> planStageBeanMap = stageBeanMap.get(planNo);
				List<String> phaseNoList = new ArrayList<>();
				List<String> phaseLenList = new ArrayList<>();
				for(String stageNo : entry.getValue().getStagenolist().split(",")) {
					phaseNoList.add(planStageBeanMap.get(stageNo).getPhasenolist());
					phaseLenList.add(planStageBeanMap.get(stageNo).getStage_green()+"");
				}
				String phaseStrs = GlobalUtil.listToStr(phaseNoList);
				String phaseLens = GlobalUtil.listToStr(phaseLenList);
				Map<String, String> planMap = new HashMap<>();
				planMap.put("coordPhaseNo", entry.getValue().getCoorphaseno());
				planMap.put("offset", entry.getValue().getOffset()+"");
				planMap.put("phaseStrs", phaseStrs);
				planMap.put("phaseLens", phaseLens);
				plansMap.put(planNo, planMap);
			}
		} else {
			dayPlanBean = new PspTrSigCtrlDayPlanBean();
			dayPlanBean.setCross_no(cross_no);
			dayPlanBean.setDayplan_name("日计划方案1");
			dayPlanBean.setDayplan_no("1");
			dayPlanBean.setId(UUID.randomUUID().toString().replace("-", ""));
			dayPlanBean.setIs_platform("0");
			dayPlanBean.setSys_id(GlobalUtil.LES);
		}
		dayPlanBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));



		List<PspTrSigCtrlDayPlanBean> dayPlans = new ArrayList<>();
		List<PspTrSigCtrlPlanBean> plans = new ArrayList<>();
		List<PspTrSigCtrlStageBean> stages = new ArrayList<>();
		List<PspTrSigCtrlDayplanPeriodBean> periods = new ArrayList<>();
		Map<String, PspTrSigCtrlPhaseBean> phaseMap = new HashMap<>();
		Map<String, String>[] timeIntervalArr = new HashMap[periodPlan.getPeriods().size()];
		int i = 0;
		int addIndex = 0;
		boolean isPeriodNew = false;
		for(PeriodInfoReply period : periodPlan.getPeriods()) {
			int periodNo = i+1;
			String planNo = "";

			List<String> phaseNoList = new ArrayList<>();
			List<String> phaseLenList = new ArrayList<>();
			for(int j=0; j < period.getStages().size(); j++) {
				int stageIndex = period.getStages().get(j);
				int totalLen = period.getStageLens().get(j);
				phaseNoList.add(stageIndex + "");
				phaseLenList.add((totalLen)+"");
			}
			String coordPhaseNo = period.getOffStage()+"";
			String offset = period.getOffset()+"";
			String phaseStrs = GlobalUtil.listToStr(phaseNoList);
			String phaseLens = GlobalUtil.listToStr(phaseLenList);
			log.info("路口查询方案:" + phaseStrs+"-"+phaseLens+"-"+coordPhaseNo+""+offset);
			boolean ifExistPlan = false;
			// 方案排序比对，从编号最小的开始比对
			List<String> planNoStrList = new ArrayList<>();
			planNoStrList.addAll(plansMap.keySet());
			planNoStrList.sort(new Comparator<String>() {
				@Override
				public int compare(String o1, String o2) {
					if(Integer.parseInt(o1) > Integer.parseInt(o2)) {
						return 1;
					}
					if(Integer.parseInt(o1) < Integer.parseInt(o2)) {
						return -1;
					}
					return 0;
				}
			});
			log.info("plansMap 大小: " + plansMap.size());
			log.info("plansMap 内容: " + plansMap);
			log.info("planNoStrList 大小: " + planNoStrList.size());
			log.info("planNoStrList 内容: " + planNoStrList);

			for(int planIndex=0; planIndex < planNoStrList.size(); planIndex++) {
				String planKey = planNoStrList.get(planIndex);
				Map<String, String> planMap = plansMap.get(planKey);
				String planCoordPhaseNo =planMap.get("coordPhaseNo");
				String planOffset = planMap.get("offset");
				String planPhaseStrs = planMap.get("phaseStrs");
				String planPhaseLens = planMap.get("phaseLens");
				log.info("当前比较方案：" + planMap);
				if(coordPhaseNo.equals(planCoordPhaseNo) && offset.equals(planOffset)
						&& phaseStrs.equals(planPhaseStrs) && phaseLens.equals(planPhaseLens)) {
					ifExistPlan = true;
					log.info("方案信息完全一致，方案合并，方案号：" + planKey);
					planNo = planKey;
					break;
				}
			}

			//			for(Entry<String, Map<String, String>> entry : plansMap.entrySet()) {
			//				Map<String, String> planMap = entry.getValue();
			//				String planCoordPhaseNo =planMap.get("coordPhaseNo");
			//				String planOffset = planMap.get("offset");
			//				String planPhaseStrs = planMap.get("phaseStrs");
			//				String planPhaseLens = planMap.get("phaseLens");
			//				log.info("当前比较方案：" + planMap);
			//				if(coordPhaseNo.equals(planCoordPhaseNo) && offset.equals(planOffset)
			//						&& phaseStrs.equals(planPhaseStrs) && phaseLens.equals(planPhaseLens)) {
			//					ifExistPlan = true;
			//					log.info("方案信息完全一致，方案合并，方案号：" + entry.getKey());
			//					planNo = entry.getKey()+"";
			//					break;
			//				}
			//			}

			List<PspTrSigCtrlStageBean> planStages = new ArrayList<>();
			if (!ifExistPlan) {
				planNo = (maxPlanNo + 1)+"";
				PspTrSigCtrlPlanBean planBean = new PspTrSigCtrlPlanBean();
				planBean.setCross_no(cross_no);
				planBean.setCoorphaseno(period.getOffStage() + "");
				planBean.setCycle_len(period.getCycleLen()+"");
				planBean.setOffset(period.getOffset()+"");
				planBean.setPlan_name("方案" + planNo);
				planBean.setPlan_no(planNo+"");
				planBean.setId(UUID.randomUUID().toString().replace("-", ""));
				planBean.setIs_platform("0");
				planBean.setCoorstageno("0");
				planBean.setSys_id(GlobalUtil.LES);
				planBean.setUpdate_time(
						LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
				List<String> stageNoList = new ArrayList<>();
				for (int j = 0; j < period.getStages().size(); j++) {
					int stageIndex = period.getStages().get(j);
					String stageNo =planNo + LesCacheUtil.seperator + stageIndex;

					stageNoList.add(stageNo);

					PspTrSigCtrlStageBean stageBean = new PspTrSigCtrlStageBean();
					stageBean.setCross_no(cross_no);
					stageBean.setStage_no(stageNo);
					stageBean.setPhasenolist(stageIndex + "");
					stageBean.setId(UUID.randomUUID().toString().replace("-", ""));
					stageBean.setFeature("0");
					stageBean.setIs_platform("0");
					stageBean.setRedyellow("0");
					stageBean.setStage_name(LesCacheUtil.phaseCodeMap.get(stageIndex + ""));
					stageBean.setIs_coorstage("0");
					if(stageIndex == period.getOffStage()) {
						planBean.setCoorstageno(stageNo);
						stageBean.setIs_coorstage("1");
					}
					stageBean.setSys_id(GlobalUtil.LES);
					stageBean.setUpdate_time(
							LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));

					int totalLen = period.getStageLens().get(j);
					int yellow = 3;
					int red = 0;
					try {
						if (null == lampConf) {
							log.info(crossID + "过渡灯色为空，灯色时长无法更新，只能默认绿灯时长为阶段时长,黄灯默认3秒");
						} else {
							yellow = lampConf.getLampConfList().get(period.getStages().get(j)).getYellow();
							red = lampConf.getLampConfList().get(period.getStages().get(j)).getClearRed();
						}
					} catch (Exception e) {
						log.error("解析过渡灯色出错：" + e, e);
					}

					stageBean.setStage_green(totalLen);
					stageBean.setStage_red(red);
					stageBean.setStage_yellow(yellow);
					phaseNoList.add(stageIndex + "");
					phaseLenList.add((totalLen + red + yellow) + "");
					planStages.add(stageBean);
					Map<String, PspTrSigCtrlStageBean> stageConf = GlobalUtil.stageConfMap.get(cross_no);
					if(null == stageConf ) {
						stageConf = new HashMap<>();
					}
					stageConf.put(stageBean.getStage_no(), stageBean);
					GlobalUtil.stageConfMap.put(cross_no, stageConf);

					PspTrSigCtrlPhaseBean phaseBean = new PspTrSigCtrlPhaseBean();
					phaseBean.setCross_no(cross_no);
					phaseBean.setId(UUID.randomUUID().toString().replace("-", ""));
					phaseBean.setPhase_no(stageIndex+"");
					phaseBean.setFeature("0");
					phaseBean.setLanenolist("");
					phaseBean.setPeddirlist("");
					phaseBean.setPhase_name(LesCacheUtil.phaseCodeMap.get(stageIndex + ""));
					phaseBean.setSys_id(GlobalUtil.LES);
					phaseBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
					phaseBean.setIs_platform("0");
					phaseBean.setNon_motor_dirs("");
					phaseBean.setPedes_dirs("");
					phaseBean.setMotor_dirs(GlobalUtil.listToStr(LesCacheUtil.getPhaseDirs(stageIndex + "")));
					phaseMap.put(stageIndex+"", phaseBean);
				}
				planBean.setStagenolist(GlobalUtil.listToStr(stageNoList));

				plans.add(planBean);
				stages.addAll(planStages);
				Map<String, String> planMap = new HashMap<>();
				planMap.put("coordPhaseNo", coordPhaseNo);
				planMap.put("offset", offset);
				planMap.put("phaseStrs", phaseStrs);
				planMap.put("phaseLens", phaseLens);
				plansMap.put(planNo, planMap);
				Map<String, PspTrSigCtrlPlanBean> planConf = GlobalUtil.planConfMap.get(cross_no);
				if(null == planConf) {
					planConf = new HashMap<>();
				}
				planConf.put(planBean.getPlan_no(), planBean);
				GlobalUtil.planConfMap.put(cross_no, planConf);

				maxPlanNo = maxPlanNo +1;
			}

			long startHour = period.getStartTime() / 60L;
			long startMin = period.getStartTime() % 60L;
			long endHour = period.getEndTime() / 60L;
			long endMin = period.getEndTime() % 60L;
			String startTime = (startHour<10?"0"+startHour:startHour) + ":" + (startMin<10?"0"+startMin:startMin)+":00";
			String endTime = (endHour<10?"0"+endHour:endHour) + ":" + (endMin<10?"0"+endMin:endMin)+":00";
			if(i >= periodPlan.getPeriods().size()-1) {
				endTime = "23:59:59";
			}
			PspTrSigCtrlDayplanPeriodBean periodBean = new PspTrSigCtrlDayplanPeriodBean();
			periodBean.setId(UUID.randomUUID().toString().replace("-", ""));
			periodBean.setCross_no(cross_no);
			periodBean.setDayplan_no(dayPlanBean.getDayplan_no());
			periodBean.setPlan_no(planNo+"");
			periodBean.setControl_mode(31+"");
			periodBean.setIs_platform("0");
			periodBean.setPeriod_endtime(endTime);
			periodBean.setPeriod_no(periodNo+"");
			periodBean.setPeriod_starttime(startTime);
			periodBean.setSys_id(GlobalUtil.LES);
			periodBean.setUpdate_time(LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
			periods.add(periodBean);
			i++;
			if(null == periodPlanList || periodPlanList.isEmpty()) {
//				timeIntervalArr[i] = new HashMap<>();
//				timeIntervalArr[i].put("IntervalNo", periodNo+"");
//				timeIntervalArr[i].put("StartTime", startTime);
//				timeIntervalArr[i].put("ConrolMode", "");
//				timeIntervalArr[i].put("EndTime", endTime);
//
//				timeIntervalArr[i].put("PlanNo", planNo+"");
				timeIntervalArr[addIndex] = new HashMap<>();
				timeIntervalArr[addIndex].put("IntervalNo", periodNo+"");
				timeIntervalArr[addIndex].put("StartTime", startTime);
				timeIntervalArr[addIndex].put("ConrolMode", "");
				timeIntervalArr[addIndex].put("EndTime", endTime);

				timeIntervalArr[addIndex].put("PlanNo", planNo+"");
				addIndex++;
			} else {
				boolean isRightPeriod = false;
				for(PspTrSigCtrlDayplanPeriodBean dayPlanPeriodBean : periodPlanList) {
					if(startTime.equals(dayPlanPeriodBean.getPeriod_starttime())
							&& endTime.equals(dayPlanPeriodBean.getPeriod_endtime())) {
						isRightPeriod = true;
					}
				}
				if(!isRightPeriod) {
					isPeriodNew = true;
					log.info("莱斯信号机路口：" + crossID + "当日配置与本地调度不一致，请立即检查，确认是否需要发送当日计划");
				}
			}
		}
		if(null == periodPlanList || periodPlanList.isEmpty()) {
		} else {
			for(PspTrSigCtrlDayplanPeriodBean periodBean : periods) {
				String periodStartTime = periodBean.getPeriod_starttime().replace(":", "");
				String periodEndTime = periodBean.getPeriod_starttime().replace(":", "");
				for(PspTrSigCtrlDayplanPeriodBean periodPlanBean : periodPlanList) {
					String periodPlanStartTime = periodPlanBean.getPeriod_starttime().replace(":", "");
					String periodPlanEndTime = periodPlanBean.getPeriod_endtime().replace(":", "");
					if(Integer.parseInt(periodStartTime) >= Integer.parseInt(periodPlanStartTime)
							&& Integer.parseInt(periodEndTime) <= Integer.parseInt(periodPlanEndTime)) {
						periodBean.setControl_mode(periodPlanBean.getControl_mode());
					}
				}
			}
		}

		String timeIntervalList = null;
		try {
			timeIntervalList = JsonUtil.objectToJsonStr(timeIntervalArr, true, JsonUtil.UPPERCAMEL);
		} catch (JsonProcessingException e) {
			log.error("莱斯日计划方案时段json转换失败");
		}
		dayPlanBean.setTimeintervallist(timeIntervalList);
		dayPlans.add(dayPlanBean);
		if(!dayPlans.isEmpty()) {
			log.info("保存莱斯日计划,路口:" + crossID + ",日计划数量：" + dayPlans.size());
			signalDao.saveSignalDayPlans(dayPlans, true);
		}
		if(!periods.isEmpty() && isPeriodNew) {
			log.info("保存莱斯日计划时段,路口:" + crossID + ",日计划时段数量：" + periods.size());
			signalDao.saveSignalDayPlanPeriods(periods, true);
		}
		if(!plans.isEmpty()) {
			log.info("保存莱斯方案,路口:" + crossID + ",方案：" + plans);
			signalDao.saveSignalPlans(plans, true);
		}
		if(!stages.isEmpty()) {
			log.info("保存莱斯阶段,路口:" + crossID + ",阶段数量：" + stages.size());
			signalDao.saveSignalStages(stages, true, "");
		}
		if(!phaseMap.isEmpty()) {
			log.info("保存莱斯相位,路口:" + crossID + ",相位数量：" + phaseMap.size());
			List<PspTrSigCtrlPhaseBean> phases = new ArrayList<>();
			phases.addAll(phaseMap.values());
			signalDao.saveSignalPhases(phases, true);
		}
		log.info("保存莱斯时段方案结束,路口:" + crossID);
	}

	public void handleControlReply(CrossControlReply controlReply) {
		String regionNo = controlReply.getRegionNo()+"";
		String crossId = controlReply.getCrossId()+"";
		String type = controlReply.getType()+"";
		if(LesCacheUtil.setLockPhase.equals(type)) {
			log.info("接收到相位锁定响应");
		} else if(LesCacheUtil.setLockControlMode.equals(type)) {
			log.info("接收到下发控制方式命令响应");
		} else {
			log.info("接收到未知类型命令响应：" + controlReply);
		}
		String key = regionNo+LesCacheUtil.seperator+crossId +LesCacheUtil.seperator  + type;
		SyncLatchModel syncLatch = LesCacheUtil.syncCaches.get(key);
		if(null == syncLatch) {
			log.info("响应超时严重，前端下发已返回失败,key:" + key);
			return;
		}
		syncLatch.setMessage(controlReply);
		syncLatch.getLatch().countDown();
	}

	public void handlePlanSetReply(CrossPlanSetReply planSetReply) {
		String key = planSetReply.getRegionNo()+LesCacheUtil.seperator+planSetReply.getCrossId() + LesCacheUtil.seperator+planSetReply.getLoadNo();
		SyncLatchModel syncLatch = LesCacheUtil.syncSetCaches.get(key);
		if(null == syncLatch) {
			log.info("本地接口已超时，莱斯设置接口实际响应时间过长");
		} else {
			syncLatch.setMessage(planSetReply);
			syncLatch.getLatch().countDown();
		}
	}

	public void handleRealMsg(final Object message) {
		if (message instanceof CrossControlModeMsg) {
			log.info("收到控制模式信息CrossControlModeMsg");
			CrossControlModeMsg controlMode = (CrossControlModeMsg)message;
			handleControlMode(controlMode);
		}
		else if (message instanceof CrossCycleMsg) {
			log.info("收到路口周期信息CrossCycleMsg");
			CrossCycleMsg cycleInfo = (CrossCycleMsg)message;
			handleCycle(cycleInfo);
		}
		else if (message instanceof CrossStageMsg) {
			log.info("收到路口阶段信息CrossStageMsg");
			CrossStageMsg stageInfo = (CrossStageMsg)message;
			handleStage(stageInfo);
		}
		else {
			log.info("unhandled message:" + message);
		}
	}

	public void handleControlMode(final CrossControlModeMsg controlMode) {
		final String crossID = String.valueOf(controlMode.getRegionNo()) + LesCacheUtil.seperator + controlMode.getCrossId();
		SignalPhaseModel phaseModel = getPhase(crossID);
		String mode = LesCacheUtil.getStdControlMode(controlMode.getControlMode() + "");
		String cross_no = crossID + GlobalUtil.FACSEPERATOR+GlobalUtil.LES;
		String state = "";
		String message = "";
		if("0".equals(mode)) {
			log.info("信号机脱机");
			state = "0";
			message = "脱机";
		} else {
			log.info("信号机在线");
			state = "1";
			message = "联机";
		}
		phaseModel.setControlMode(mode);
		phaseModel.setState(state);
		phaseModel.setDataTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		LesCacheUtil.cachedPhase.put(crossID, phaseModel);
		String controlKey = crossID + LesCacheUtil.seperator + LesCacheUtil.CONTROLMODEPUSH;
		SyncLatchModel syncControlModeLatch = LesCacheUtil.syncCaches.get(controlKey);
		if(null == syncControlModeLatch) {
			log.info("区域路口：" + crossID + "未执行临时控制");
		} else {
			syncControlModeLatch.setMessage(mode);
			syncControlModeLatch.getLatch().countDown();
		}

		try {
			Map<String, String> stateRecordMap = new HashMap<>();
			stateRecordMap.put("crossNo", cross_no);
			stateRecordMap.put("state", state);
			stateRecordMap.put("recordTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
			stateRecordMap.put(GlobalUtil.HIS_TYPE, GlobalUtil.HIS_STATE);
			SignalHisRecordJob.hisRecordQueue.put(stateRecordMap);

			Map<String, String> recordMap = new HashMap<>();
			recordMap.put("crossNo", cross_no);
			recordMap.put("planNo", "");
			recordMap.put("stageNo", "");
			recordMap.put("stageLen", "");
			String startTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter));
			recordMap.put("stageStartTime", startTime);
			recordMap.put("recordTime", startTime);
			recordMap.put("controlMode", mode);
			recordMap.put(GlobalUtil.HIS_TYPE, GlobalUtil.HIS_STAGE);
			SignalHisRecordJob.hisRecordQueue.put(recordMap);

			Map<String, String> stateMap = new HashMap<>();
			stateMap.put(GlobalUtil.RUNNINGTYPE_KEY, GlobalUtil.RUNNINGTYPE_STATE);
			stateMap.put("crossNo", cross_no);
			stateMap.put("state", state);
			stateMap.put("message", message);
			GlobalUtil.queue.put(stateMap);
			SignalMessageJob.stateQueue.put(stateMap);


			Map<String, String> runningStageMap = GlobalUtil.stageRunningMap.get(cross_no);
			if(null == runningStageMap) {
				runningStageMap = new HashMap<>();
			}
			runningStageMap.put("cross_no", cross_no);
			runningStageMap.put("sys_id", GlobalUtil.LES);
			runningStageMap.put("update_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
			runningStageMap.put(GlobalUtil.RUNNINGTYPE_KEY, GlobalUtil.RUNNINGTYPE_STAGE);
			runningStageMap.put("control_mode", phaseModel.getControlMode());
			runningStageMap.put("cross_state", phaseModel.getState());
			GlobalUtil.stageRunningMap.put(cross_no, runningStageMap);
		} catch (Exception e) {
			log.error("准备保存路口状态记录发生错误：" + e, e);
		}
	}

	public void handleCycle(final CrossCycleMsg cycleInfo) {
		final String key = String.valueOf(cycleInfo.getRegionNo()) + LesCacheUtil.seperator + cycleInfo.getCrossId();
		SignalPhaseModel phaseModel = getPhase(key);
		phaseModel.setDataTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		phaseModel.setCycleLen(cycleInfo.getLastCycleLen());
		//        phaseModel.setCycleStartTime(getCycleStart(cycleInfo.getStartTime()));
		phaseModel.setCycleStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		LesCacheUtil.cachedPhase.put(key, phaseModel);
		log.info("phaseModel:" + phaseModel);
	}

	public void handleStage(final CrossStageMsg stageInfo) {
		String crossID = stageInfo.getRegionNo() + LesCacheUtil.seperator + stageInfo.getCrossId();
		String crossNo = crossID + GlobalUtil.FACSEPERATOR + GlobalUtil.LES;
		SignalPhaseModel phaseModel = getPhase(crossID);
		phaseModel.setCurPhaseLen(stageInfo.getCurStageLen());
		phaseModel.setCurPhaseNo(stageInfo.getCurStageNo()+"");
		phaseModel.setDataTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		phaseModel.setLastPhaseNo(stageInfo.getLastStageNo()+"");
		phaseModel.setLastPhaseLen(stageInfo.getLastStageLen());
		phaseModel.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		phaseModel.setEndTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(new Date().getTime()+stageInfo.getCurStageLen()*1000)));

		Map<String, String> runningMap = new HashMap<>();
		runningMap.put("cross_no", crossNo);
		runningMap.put("stage_no", phaseModel.getCurPhaseNo());
		runningMap.put("stage_start_time", phaseModel.getStartTime());
		runningMap.put("sys_id", GlobalUtil.LES);
		runningMap.put("update_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
		runningMap.put(GlobalUtil.RUNNINGTYPE_KEY, GlobalUtil.RUNNINGTYPE_STAGE);
		runningMap.put("cycle_start_time", phaseModel.getCycleStartTime());
		runningMap.put("control_mode", phaseModel.getControlMode());
		try {
			//			PeriodInfoReply period = LesCacheUtil.cachePeriodPlan.get(key).getCurPeriod(
			//							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(phaseModel.getCycleStartTime()).getTime());
			String planNo = "1";
			String lastPlanNo = "";
			PspTrSigCtrlPlanBean planBean = null;
			List<PspTrSigCtrlDayplanPeriodBean> tempPeriodPlanList = signalDao.qrySignalDayPlanPeriods(crossNo, "0");
			if(null != tempPeriodPlanList && !tempPeriodPlanList.isEmpty()) {
				log.info("路口当前处于本地调度临时控制，方案号：" + tempPeriodPlanList.get(0).getPlan_no());
				List<PspTrSigCtrlPlanBean> tempPlanList = signalDao.qrySignalPlans(crossNo, tempPeriodPlanList.get(0).getPlan_no());
				if(null == tempPlanList || tempPlanList.isEmpty()) {
					log.info("当前路口处于临时调度，但方案表中未查询到临时调度方案，数据丢弃");
					return ;
				}
				planBean = tempPlanList.get(0);
				planNo = planBean.getPlan_no();
				lastPlanNo = planBean.getPlan_no();
			} else {
				Map<String, Object> curPlanMap = getCurScheduleDayPlan(crossID);
				if(curPlanMap.isEmpty() || !curPlanMap.containsKey("dayPlan") || !curPlanMap.containsKey("dayPlanPeriodList")) {
					log.info("未查到当日调度，无法匹配当前方案,默认方案1");
					planNo = "1";
				} else {
					List<PspTrSigCtrlDayplanPeriodBean> periodPlanList = (List<PspTrSigCtrlDayplanPeriodBean>) curPlanMap.get("dayPlanPeriodList");
					if(periodPlanList.isEmpty()) {
						log.info("当前日计划没有配置时段，无法匹配方案,默认方案1");
						planNo = "1";
					} else {
						for(PspTrSigCtrlDayplanPeriodBean periodBean : periodPlanList) {
							String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.timeFormatter)).replace(":", "");
							String lastStageTime = LocalDateTime.now().minusSeconds(stageInfo.getLastStageLen())
									.format(DateTimeFormatter.ofPattern(GlobalUtil.timeFormatter)).replace(":", "");

							String startTime = periodBean.getPeriod_starttime().replace(":", "");
							String endTime = periodBean.getPeriod_endtime().replace(":", "");
							if (Integer.parseInt(curTime) < Integer.parseInt(endTime)
									&& Integer.parseInt(curTime) >= Integer.parseInt(startTime)) {
								planNo = periodBean.getPlan_no();
								planBean = ((Map<String, PspTrSigCtrlPlanBean>)curPlanMap.get("planMap")).get(planNo);
							}
							if(Integer.parseInt(lastStageTime) < Integer.parseInt(endTime)
									&& Integer.parseInt(lastStageTime) >= Integer.parseInt(startTime)) {
								lastPlanNo = periodBean.getPlan_no();
							}
						}
					}
				}
			}

			String curStageNo = planNo + LesCacheUtil.seperator + phaseModel.getCurPhaseNo();
			phaseModel.setPlanNo(planNo);
			runningMap.put("plan_no", planNo);
			runningMap.put("stage_no", curStageNo);
			if(null != planBean) {
				List<Map<String, Object>> phaseLampList = new ArrayList<>();
				for(String stageNo : planBean.getStagenolist().split(",")) {
					Map<String, Object> map = new HashMap<>();
					map.put("phaseNo", stageNo.split(LesCacheUtil.seperator)[1]);
					if(stageNo.equals(curStageNo)) {
						map.put("lampStatus", "23");
					} else {
						map.put("lampStatus", "21");
					}
					phaseLampList.add(map);
				}
				runningMap.put("phase_lampstatus", JsonUtil.objectToJsonStr(phaseLampList));
				phaseModel.setPhaseLampStatus(phaseLampList);
			}
			phaseModel.setPhaseStatusTime(LocalDateTime.now());
			GlobalUtil.queue.put(runningMap);

			Map<String, String> runningStageMap = GlobalUtil.stageRunningMap.get(crossNo);
			if(null == runningStageMap) {
				runningStageMap = new HashMap<>();
			}
			runningStageMap.put("cross_no", crossNo);
			runningStageMap.put("stage_no", phaseModel.getCurPhaseNo());
			runningStageMap.put("stage_start_time", phaseModel.getStartTime());
			runningStageMap.put("sys_id", GlobalUtil.LES);
			runningStageMap.put("update_time", LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter)));
			runningStageMap.put("cycle_start_time", phaseModel.getCycleStartTime());
			runningStageMap.put("control_mode", phaseModel.getControlMode());
			runningStageMap.put("cross_state", phaseModel.getState());
			runningStageMap.put("plan_no", planNo);
			runningStageMap.put("stage_no", curStageNo);
			GlobalUtil.stageRunningMap.put(crossNo, runningStageMap);


			if (lastControlModeMap.containsKey(crossID)) {
				Map<String, String> recordMap = new HashMap<>();
				recordMap.put("crossNo", crossNo);
				recordMap.put("planNo", lastPlanNo);
				recordMap.put("stageNo", lastPlanNo + LesCacheUtil.seperator + stageInfo.getLastStageNo());
				recordMap.put("stageLen", stageInfo.getLastStageLen() + "");
				String stageStartTime = LocalDateTime
						.parse(phaseModel.getStartTime(), DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter))
						.minusSeconds(stageInfo.getLastStageLen())
						.format(DateTimeFormatter.ofPattern(GlobalUtil.datetimeFormatter));
				recordMap.put("stageStartTime", stageStartTime);
				recordMap.put("recordTime", phaseModel.getStartTime());
				recordMap.put("controlMode",
						!lastControlModeMap.containsKey(crossID) ? "31" : lastControlModeMap.get(crossID));
				recordMap.put(GlobalUtil.HIS_TYPE, GlobalUtil.HIS_STAGE);
				SignalHisRecordJob.hisRecordQueue.put(recordMap);
			}
			lastControlModeMap.put(crossID, phaseModel.getControlMode());
		} catch (Exception e) {
			log.error("获取日方案中当前运行方案失败" + e);
		}
		LesCacheUtil.cachedPhase.put(crossID, phaseModel);
	}

	public void handlePeriodPlanMsg(final PeriodPlanReply reply) {
		log.info("接收到日时段方案信息，做当日调度处理");
		String regionNo = reply.getRegionNo()+"";
		String crossId = reply.getCrossId()+"";

		String crossID = regionNo + LesCacheUtil.seperator + crossId;
		LesCacheUtil.cachePeriodPlan.put(crossID, reply);
		LampConfReply lampConf = LesCacheUtil.cacheLampConf.get(crossID); //  reply
		if(null == reply || null == lampConf) {
			log.warn("未接收到莱斯过渡灯色配置，暂不更新配置");
			return;
		}
		savePeriodPlanWithLamp(crossID, reply, lampConf);
	}

	public void handleLampInfo(final LampConfReply reply) {
		log.info("接收到过渡灯色配置");
		String regionNo = reply.getRegionNo()+"";
		String crossId = reply.getCrossId()+"";

		String crossID = regionNo + LesCacheUtil.seperator + crossId;
		LesCacheUtil.cacheLampConf.put(crossID, reply);
		PeriodPlanReply periodPlan = LesCacheUtil.cachePeriodPlan.get(crossID); //  reply
		if(null == reply || null == periodPlan) {
			log.warn("未收到莱斯当日方案配置，暂不更新配置");
			return;
		}
		savePeriodPlanWithLamp(crossID, periodPlan, reply);
	}

	public void handleFault(final QueryFaultReply reply) {
		final byte msgType = reply.getMsgType();
		final byte regionNo = reply.getRegionId();
		final int crossId = reply.getCrossId();
		final byte fault = reply.getCode();
		switch (fault) {
			case 1: {
				log.info("区域号不正确，请检查区域信息，regionNo:" + regionNo);
				break;
			}
			case 2: {
				log.info("路口号超出范围,请检查路口号，crossId:" + crossId);
				break;
			}
			case 3: {
				log.info("信息类型错误,请检查，msgType:" + msgType);
				break;
			}
			default: {
				log.info("未知错误类型，请检查并核对协议" + reply);
				break;
			}
		}
	}

	private String getCycleStart(final long cycleStartTime) {
		String cycleStart = LocalDate.now().format(DateTimeFormatter.ofPattern(GlobalUtil.dateFormatter));
		long h = cycleStartTime / 3600L;
		if(h>= 24) {
			cycleStart = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern(GlobalUtil.dateFormatter)) + " ";
			h = h-24;
		}
		cycleStart +=" ";
		if (h < 10L) {
			cycleStart = String.valueOf(cycleStart) + "0";
		}
		cycleStart = String.valueOf(cycleStart) + h + ":";
		final long m = (cycleStartTime - h * 60L * 60L) / 60L;
		if (m < 10L) {
			cycleStart = String.valueOf(cycleStart) + "0";
		}
		cycleStart = String.valueOf(cycleStart) + m + ":";
		final long s = cycleStartTime % 60L;
		if (s < 10L) {
			cycleStart = String.valueOf(cycleStart) + "0";
		}
		cycleStart = String.valueOf(cycleStart) + s;
		return cycleStart;
	}

	private SignalPhaseModel getPhase(final String key) {
		SignalPhaseModel phaseModel = LesCacheUtil.cachedPhase.get(key);
		if (phaseModel == null) {
			phaseModel = new SignalPhaseModel();
			phaseModel.setRegionNo(key.split(LesCacheUtil.seperator)[0]);
			phaseModel.setCrossNo(key);
			phaseModel.setDataTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		}
		return phaseModel;
	}

	public Map<String, Object> getCurScheduleDayPlan(String crossID) {
		Map<String, Object> curPlanMap = new HashMap<>();
		String crossNo = crossID + GlobalUtil.FACSEPERATOR + GlobalUtil.LES;
		List<PspTrSigCtrlDispatchPlanBean> dispatchList = signalDao.qrySignalDispatchPlans(crossNo, null);
		List<PspTrSigCtrlDayplanPeriodBean> periodPlanList = new ArrayList<>(); // 当日时段
		Map<String, PspTrSigCtrlPlanBean> curDatePlanMap = new HashMap<>(); // 当日方案
		String dayPlanNo = "";
		if(null == dispatchList || dispatchList.isEmpty()) {
			log.info("当前路口：" + crossNo + "无调度信息，默认使用的是当前的1号调度");
			dayPlanNo = "1";
		} else {
			dayPlanNo = getCurDayPlanNo(dispatchList);
			curPlanMap.put("schedule", "yes");
		}
		if(StringUtils.isEmpty(dayPlanNo)) {
			log.info("当前调度信息中缺失今日调度，默认使用1号调度");
			dayPlanNo = "1";
		}

		List<PspTrSigCtrlDayPlanBean> dayPlanList = signalDao.qrySignalDayPlans(crossNo, dayPlanNo);
		List<PspTrSigCtrlDayplanPeriodBean> dayPlanPeriodList = signalDao.qrySignalDayPlanPeriods(crossNo, dayPlanNo);
		List<PspTrSigCtrlPlanBean> planBeanList = signalDao.qrySignalPlans(crossNo, null);
		List<PspTrSigCtrlStageBean> stageBeanList = signalDao.qrySignalStages(crossNo, null);
		Map<String, PspTrSigCtrlPlanBean> planMap = new HashMap<>(); // 当前所有方案
		for(PspTrSigCtrlPlanBean planBean : planBeanList) {
			String planNo = planBean.getPlan_no();
			if(null == planNo || "".equals(planNo) || planNo.contains("temp") || "null".equalsIgnoreCase(planNo)) {
				log.info("方案表中路口：" + crossNo + "存在临时方案");
				continue;
			}
			planMap.put(planBean.getPlan_no(), planBean);
			Map<String, PspTrSigCtrlPlanBean> planConf = GlobalUtil.planConfMap.get(crossNo);
			if(null == planConf) {
				planConf = new HashMap<>();
			}
			planConf.put(planBean.getPlan_no(), planBean);
			GlobalUtil.planConfMap.put(crossNo, planConf);
		}
		Map<String, Map<String, PspTrSigCtrlStageBean>> stageMap = new HashMap<>(); // 当前所有阶段，按方案分组
		for(PspTrSigCtrlStageBean stageBean : stageBeanList) {
			String planNo = stageBean.getStage_no().split(LesCacheUtil.seperator)[0];
			Map<String, PspTrSigCtrlStageBean> planStageMap = stageMap.get(planNo);
			if(null == planStageMap) {
				planStageMap = new HashMap<>();
			}
			planStageMap.put(stageBean.getStage_no(), stageBean);
			stageMap.put(planNo, planStageMap);
			Map<String, PspTrSigCtrlStageBean> stageConf = GlobalUtil.stageConfMap.get(crossNo);
			if(null == stageConf ) {
				stageConf = new HashMap<>();
			}
			stageConf.put(stageBean.getStage_no(), stageBean);
			GlobalUtil.stageConfMap.put(crossNo, stageConf);
		}
		if(null == dayPlanList || dayPlanList.isEmpty()) {
			log.info("日计划方案表中无默认1号计划，需要新增日计划");
		} else {
			curPlanMap.put("dayPlan", dayPlanList.get(0)); // 当日日计划方案
		}
		log.info(crossNo+"日计划时段方案:"+dayPlanPeriodList);
		if(null == dayPlanPeriodList || dayPlanPeriodList.isEmpty()) {
			log.info(crossNo+"日计划时段为空，需要新增");
		} else {
			for(PspTrSigCtrlDayplanPeriodBean dayPlanPeriodBean : dayPlanPeriodList) {
				String startTime = dayPlanPeriodBean.getPeriod_starttime().replace(":", "");
				String endTime = dayPlanPeriodBean.getPeriod_endtime().replace(":", "");
				String curTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.timeFormatter)).replace(":", "");
				boolean ifCurPeriod = Integer.parseInt(curTime) >= Integer.parseInt(startTime)
						&& Integer.parseInt(curTime) < Integer.parseInt(endTime);
				PspTrSigCtrlPlanBean planBean = planMap.get(dayPlanPeriodBean.getPlan_no());
				if(ifCurPeriod) {
					curPlanMap.put("curPlan", planBean);
					List<PspTrSigCtrlStageBean> curStageList = new ArrayList<>();
					for(String stageNo : planBean.getStagenolist().split(",")) {
						curStageList.add(stageMap.get(planBean.getPlan_no()).get(stageNo));
					}
					curPlanMap.put("curStageList", curStageList);
				}
				curDatePlanMap.put(planBean.getPlan_no(), planBean);
				periodPlanList.add(dayPlanPeriodBean);
			}
		}

		curPlanMap.put("curDatePlanMap", curDatePlanMap);
		curPlanMap.put("dayPlanPeriodList", periodPlanList);
		curPlanMap.put("stageMap", stageMap);
		curPlanMap.put("planMap", planMap);

		return curPlanMap;
	}

	private String getCurDayPlanNo(List<PspTrSigCtrlDispatchPlanBean> dispatchList) {
		String dayPlanNo = "";
		int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1 ;
		String curDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern(GlobalUtil.monthDayFormatter));
		if(week <= 0) {
			week = 7;
		}
		for(PspTrSigCtrlDispatchPlanBean dispatchBean : dispatchList) {
			long dispatchType = dispatchBean.getDispatchplan_type();
			boolean isSpecial = false;
			if(1 == dispatchType) {
				if(dispatchBean.getDispatch_week().equals(week+"") && "".equals(dayPlanNo)) {
					dayPlanNo = dispatchBean.getDayplanno();
				}
			} else {
				String dispatchDate = dispatchBean.getDispatch_date();
				for(String date : dispatchDate.split(",")) {
					if(date.equals(curDate)) {
						dayPlanNo = dispatchBean.getDayplanno();
						break;
					}
				}
			}
			if(isSpecial) {
				break;
			}
		}
		return dayPlanNo;
	}


	public CommonSignalResponseBean setPlanOffset(String crossNo, CrossSetPlanReq planSetReq, CrossSetPlanOffSetReq offsetReq) {
		String crossID = crossNo.split(GlobalUtil.FACSEPERATOR)[0];
		String planSetKey = crossID + LesCacheUtil.seperator + planSetReq.getLoadNo();
		String offsetSetKey = crossID + LesCacheUtil.seperator + offsetReq.getLoadNo();
		IoSession session = LesCacheUtil.sessionMap.get(crossID.split(LesCacheUtil.seperator)[0]);
		if(null == session || session.isClosing()) {
			log.error("区域机：" + crossID + "尚未登录，无法下发方案修改命令");
			return GlobalUtil.getResponse(0,null,"区域机"+crossID+"未连接，无法执行方案设置");
		}
		SyncLatchModel syncSetLatch = new SyncLatchModel();
		try {
			LesCacheUtil.syncSetCaches.put(planSetKey, syncSetLatch);
			session.write(planSetReq);
			syncSetLatch.getLatch().await(15, TimeUnit.SECONDS);
			if (null == syncSetLatch.getMessage()) {
				log.error("路口：" + crossID + "方案修改命令响应超时");
				return GlobalUtil.getResponse(0, null, "莱斯接口超时，未响应方案修改命令");
			}
			CrossPlanSetReply planSetReply = (CrossPlanSetReply) syncSetLatch.getMessage();

			if (0 == planSetReply.getResult()) {  // 0成功，1错误
				log.info("方案设置返回成功，路口：" + crossID + ",设置方案号：" + planSetReply.getPlanNo());
				try{
					SyncLatchModel syncSetOffsetLatch = new SyncLatchModel();
					LesCacheUtil.syncSetCaches.put(offsetSetKey, syncSetOffsetLatch);

					session.write(offsetReq);
					syncSetOffsetLatch.getLatch().await(15, TimeUnit.SECONDS);
					if (null == syncSetOffsetLatch.getMessage()) {
						log.error("路口：" + crossID + "方案协调信息修改命令响应超时");
						return GlobalUtil.getResponse(1, null, "方案设置成功,但协调信息设置超时失败");
					}
					CrossPlanSetReply planSetOffsetReply = (CrossPlanSetReply) syncSetOffsetLatch.getMessage();

					if (0 == planSetOffsetReply.getResult()) {  // 0成功，1错误
						log.info("方案协调信息设置返回成功，路口：" + crossID);
						return GlobalUtil.getResponse(1, null, "方案设置成功");
					} else {
						return GlobalUtil.getResponse(1, null, "方案设置成功,但协调信息设置执行失败");
					}

				} finally {
					LesCacheUtil.syncSetCaches.remove(offsetSetKey); // 清理同步参数缓存
				}
			} else {
				log.info("方案设置返回失败，路口：" + crossID + ",修改方案配置失败，方案号：" + planSetReply.getPlanNo());
				return GlobalUtil.getResponse(0, null, "方案设置失败");
			}
		} catch(Exception e) {
			log.error("方案设置发生错误，请检查：" + e, e);
		} finally {
			LesCacheUtil.syncSetCaches.remove(planSetKey); // 清理同步参数缓存
		}
		return GlobalUtil.getResponse(0, null, "方案设置发生程式错误");
	}

	public CommonSignalResponseBean setPeriodPlan(String crossNo, CrossSetPeriodReq periodSetReq) {
		String crossID = crossNo.split(GlobalUtil.FACSEPERATOR)[0];
		String periodSetKey = crossID + LesCacheUtil.seperator + periodSetReq.getLoadNo();
		IoSession session = LesCacheUtil.sessionMap.get(crossID.split(LesCacheUtil.seperator)[0]);
		if(null == session || session.isClosing()) {
			log.error("区域机：" + crossID + "尚未登录，无法下发时段方案修改命令");
			return GlobalUtil.getResponse(0,null,"区域机"+crossID+"未连接，无法执行时段方案设置");
		}
		SyncLatchModel syncSetLatch = new SyncLatchModel();
		try {
			LesCacheUtil.syncSetCaches.put(periodSetKey, syncSetLatch);
			session.write(periodSetReq);
			syncSetLatch.getLatch().await(15, TimeUnit.SECONDS);
			if (null == syncSetLatch.getMessage()) {
				log.error("路口：" + crossID + "时段方案修改命令响应超时");
				return GlobalUtil.getResponse(0, null, "莱斯接口超时，未响应时段方案修改命令");
			}
			CrossPlanSetReply planSetReply = (CrossPlanSetReply) syncSetLatch.getMessage();

			if (0 == planSetReply.getResult()) {  // 0成功，1错误
				log.info("时段方案设置返回成功，路口：" + crossID);

				return GlobalUtil.getResponse(1, null, "时段方案设置成功，路口开始执行");
			} else {
				log.info("时段方案设置返回失败，路口：" + crossID + ",修改时段方案配置失败");
				return GlobalUtil.getResponse(0, null, "时段方案设置返回失败");
			}
		} catch(Exception e) {
			log.error("方案设置发生错误，请检查：" + e, e);
		}
		finally {
			LesCacheUtil.syncSetCaches.remove(periodSetKey); // 清理同步参数缓存
		}
		return GlobalUtil.getResponse(0, null, "方案设置发生程式错误");
	}

}
*/
