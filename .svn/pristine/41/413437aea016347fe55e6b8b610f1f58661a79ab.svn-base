package com.contactsImprove.utils;

import java.util.Date;
import com.contactsImprove.entity.admin.Wallet;
import com.contactsImprove.service.admin.WalletService;

public class CoinScanJob extends Thread {

	private long timeout = 15 * 60 * 1000; //15分钟执行扫描区块

	private static WalletService walletService;
	
	private static USDTUtils usdtUtils = null;
	
	public static void initCoinScanJob(WalletService walletService,USDTUtils usdtUtils) {
		CoinScanJob.walletService = walletService;
		CoinScanJob.usdtUtils = usdtUtils;
	}
	
	private byte[] lock = new byte[0];

	public void run() {
		while (true) {
			synchronized (lock) {
				try {
					usdtJob();// USDT区块扫描
					usdtUtils.Transaction();// USDT归集
					lock.wait(timeout);
				} catch (Exception e) {
					LoggerUtil.error(e.toString(), e);
				}
			}
		}
	}

	/**
	 * USDT 定时扫描区块
	 */
	private void usdtJob() {
		Wallet wallet = walletService.selectWalletByType(1);
		if (wallet.getBlock() == null)
			return;
		int blockCount = usdtUtils.getBlockCount();// 获取USTD区块高度
		if (blockCount > wallet.getBlock()) {
			int index = Integer.valueOf(wallet.getBlock().toString());
			while (index < blockCount) {
				try {
					if (usdtUtils.parseBlock(index)) {
						index++;
					} else {
						index++;
						continue;
					}
				} catch (Exception e) {
					index++;
					LoggerUtil.error(e.toString(), e);
				}
			}
			Wallet wt = new Wallet();
			wt.setId(wallet.getId());
			wt.setBlock((long)blockCount);
			wt.setUpdateTime(new Date());
			walletService.updateByPrimaryKeySelective(wt);
		}
	}
}
