/*
 *      Copyright (c) 2018-2028, Chill Zhuang All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Chill 庄骞 (smallchill@163.com)
 */
package xs.rfid.rule;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;

import java.io.File;
import java.util.Date;

/**
 * 默认存储桶生成规则
 *
 * @author Chill
 */
@AllArgsConstructor
public class RfidOssRule implements OssRule {


	@Override
	public String bucketName(String bucketName) {

		return bucketName;
	}

	@Override
	public String fileName(String originalFilename) {
		String fileName = new File(originalFilename).getName();
		int dotIndex = fileName.lastIndexOf('.');
		String fix = (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
		return "upload" + "/" + DateUtil.format(new Date(), "yyyyMMdd") + "/" + StrUtil.uuid() + "." + fix;
	}

}
