function validateQQGroup($qq_ids, $qq_names, isBigQQ) {
	var result = false, message = "";
	if ($qq_ids.filter(":blank").length != $qq_names.filter(":blank").length) {
		message = "请填写对应的 QQ 账号信息。"
		result = false;
	} else {
		result = true;
	}
	if (result) {
		var name, code;
		for (var i = 0, count = $qq_ids.length; i < count; i++) {
			name = $qq_names.get(i).value;
			code = $qq_ids.get(i).value;
			if (name === "" && code === "") continue;
			if (name.length > 10) {
				message = "显示名称长度小于10个字符。";
				result = false;
				break;
			}
			if (isBigQQ) {
				if (!/^(800|400)\d{6}$/.test(code)) {
					message = "请填写正确的企业 QQ 400|800 账号";
					result = false;
					break;
				}
			} else if (!/^\d{5,11}$/.test(code)) {
				message = "请填写正确的  QQ 账号";
				result = false;
				break;
			}
		}
	}
	if (!result) {
		return message;
	} else {
		return true;
	}
}
