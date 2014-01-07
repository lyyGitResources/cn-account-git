(function($) { 
	// options:domId必须大于等于0，为0表示没选
    $.fn.domainArea = function(options) { 
    	var domChildArray = domainAreaChildList();
    	var domListArray = domainAreaList();
		var domId = options.domId > 0 ? options.domId : 0;
		
		var domPath = getDomPath(domId, domListArray);
		
		getOneSelect(domPath[0], domChildArray, domListArray, $(this));
		getTwoSelect(domPath[1], domPath[0], domChildArray, domListArray);
		getThreeSelect(domPath[2], domPath[1], domChildArray, domListArray);

		$("#_domOne").change(function(){
    		getTwoSelect(-1, $(this).val(), domChildArray, domListArray);
    		$("#_domThree").val(-1);
//   		$("#_domThree").attr("disabled", "disabled");
    	});
    	$("#_domTwo").change(function(){
    		getThreeSelect(-1, $(this).val(), domChildArray, domListArray);
    	});
    };

    function getOneSelect(domId, domChildArray, domListArray, $this){
    	var buffer = "<select id='_domOne'>";
    	buffer += "<option value='' selected>请选择</option>";
    	for (var i = 0; i < domChildArray[0].length; i++) {
   			var dom = domListArray[domChildArray[0][i]];
			buffer += "<option value='" + dom.id + "' ";
			if(dom.id == domId){
				buffer += "selected ";
			}
			buffer += ">"+ dom.name + "</option>";
		}
		buffer += "</select>";
		$this.append(buffer);
    };
    
    function getTwoSelect(domId, parentId, domChildArray, domListArray){
    	var domTwo = domListArray[parentId];
    	// 重新生成产业带
    	if($("#_domTwo").attr("id")){
    		// 移除原先的数据
    		removeOption("_domTwo");
    		
    		// $("#_domTwo").removeAttr("disabled");
    		
    		if(domTwo && domTwo.cIds  > -1){
	    		for (var i = 0; i < domChildArray[domTwo.cIds].length; i++) {
	    			var dom = domListArray[domChildArray[domTwo.cIds][i]];
					$("<option value='" + dom.id + "'>" + dom.name + "</option>").appendTo($("#_domTwo"));
				}
			}
		// 新生成产业带
    	}else{
	    	var buffer = "";
	    	buffer += "&nbsp;&nbsp;<select id='_domTwo' >";
	    	buffer += "<option value='' selected>请选择</option>";
	
	    	if(domTwo && domTwo.cIds  > -1){
		    	for (var i = 0; i < domChildArray[domTwo.cIds].length; i++) {
		    		var dom = domListArray[domChildArray[domTwo.cIds][i]];
					buffer += "<option value='" + dom.id + "' ";
					if(dom.id == domId){
						buffer += "selected ";
						buffer = buffer.replace(/disabled/, "");
					}
					buffer += ">"+ dom.name + "</option>";
				}
	    	}
	    	buffer += "</select>";
	    	$("#_domOne").after(buffer);
    	}
    };
    
    function getThreeSelect(domId, parentId, domChildArray, domListArray){
    	var domThree = domListArray[parentId];
    	
    	if($("#_domThree").attr("id")){
    		removeOption("_domThree");
//    		$("#_domThree").removeAttr("disabled");
    		if(domThree && domThree.cIds  > -1){
		    	for (var i = 0; i < domChildArray[domThree.cIds].length; i++) {
		    		var dom = domListArray[domChildArray[domThree.cIds][i]];
					$("<option value='" + dom.id + "'>" + dom.name + "</option>").appendTo($("#_domThree"));
				}
	    	}
    	}else{
    		var buffer = "";
	    	buffer += "&nbsp;&nbsp;<select id='_domThree' name='domId' >";
	    	buffer += "<option value='' selected>请选择</option>";

	    	if(domThree && domThree.cIds  > -1){
		    	for (var i = 0; i < domChildArray[domThree.cIds].length; i++) {
		    		var dom = domListArray[domChildArray[domThree.cIds][i]];
					buffer += "<option value='" + dom.id + "' ";
					if(dom.id == domId){
						buffer += "selected ";
						buffer = buffer.replace(/disabled/, "");
					}
					buffer += ">"+ dom.name + "</option>";
				}
	    	}
	    	buffer += "</select>";
	    	$("#_domTwo").after(buffer);
    	}
    };
    
    /**
	 * 得到最底层产业带的产业带ID路径,domPath[0] 第一层 domPath[1] 第二层 domPath[2] 第三层
	 * @param domId
	 * @param domListArray
	 */
    function getDomPath(domId, domListArray){
    	// 一层
    	var domPath = [-1,-1,-1];
    	// 两层
    	if(domListArray[domId].pId > 0 && domListArray[domListArray[domId].pId].pId == 0){
    		domPath[0] = domListArray[domId].pId;
    		domPath[1] = domId;
    		return domPath;
    	}
    	// 三层
    	if(domListArray[domListArray[domId].pId].pId > 0){
    		domPath[0] = domListArray[domListArray[domId].pId].pId;
    		domPath[1] = domListArray[domId].pId;
    		domPath[2] = domId;
    		return domPath;
    	}
    	return domPath;
    }
 
     /**
	 * 移除除‘请选择’以外的项
	 * @param selectId
	 */
    function removeOption(selectId){
		$("#" + selectId + " option").each(function(){
			if($(this).val() != ''){
				$(this).remove();
			}
		});
	}
	
    /**
	 * 子产业带ID数组
	 * 1) 存储所有子产业带ID列表数据
	 * 2) 子产业带ID列表所在位置存在domList
	 */
	function domainAreaChildList(){
	    var c = new Array();
	    c.push([1,99,171,21,125,47,143,77]);
	    c.push([17,2,12,8]);
	    c.push([3,4,5,6,7]);
	    c.push([9,11,10]);
	    c.push([16,15,14,13]);
	    c.push([18,19,20]);
	    c.push([41,35,22,31,28]);
	    c.push([23,24,27,26,25]);
	    c.push([30,29]);
	    c.push([33,34,32]);
	    c.push([36,37,40,39,38]);
	    c.push([43,46,45,44,42]);
	    c.push([65,71,54,48,60]);
	    c.push([52,51,50,49,53]);
	    c.push([55,58,56,59,57]);
	    c.push([61,64,63,62]);
	    c.push([66,70,69,68,67]);
	    c.push([73,75,76,72,74]);
	    c.push([96,83,78,88,92]);
	    c.push([79,81,80,82]);
	    c.push([87,86,85,84]);
	    c.push([91,90,89]);
	    c.push([93,95,94]);
	    c.push([98,97]);
	    c.push([121,116,110,100,105]);
	    c.push([103,102,101,104]);
	    c.push([106,109,108,107]);
	    c.push([112,114,115,111,113]);
	    c.push([120,117,118,119]);
	    c.push([124,123,122]);
	    c.push([126,141,138,131,135]);
	    c.push([130,127,128,129]);
	    c.push([133,134,132]);
	    c.push([136,137]);
	    c.push([140,139]);
	    c.push([142]);
	    c.push([160,166,150,156,144]);
	    c.push([149,147,146,145,148]);
	    c.push([155,154,153,152,151]);
	    c.push([157,158,159]);
	    c.push([161,165,164,163,162]);
	    c.push([170,169,168,167]);
	    c.push([196,193,178,188,172,183]);
	    c.push([176,177,173,174,175]);
	    c.push([181,182,180,179]);
	    c.push([187,186,185,184]);
	    c.push([192,191,190,189]);
	    c.push([194,195]);
	    c.push([197,198,199]);
	    return c;
	}
	/**
	* 产业带数组
	* 1) 产业带ID为0表示一个根产业带
	* 2) 数组下标与产业带ID相同，即domList[100]是产业带ID为100的产业带
	* 3) cIds表示子产业带列表在cList数组中的位置
	*/
  	function domainAreaList(){
	    var d = new Array();
	    d.push({id:0,name:'',pId:0,cIds:0});
	    d.push({id:1,name:'机械及工业制品',pId:0,cIds:1});
	    d.push({id:2,name:'五金',pId:1,cIds:2});
	    d.push({id:3,name:'永康',pId:2,cIds:-1});
	    d.push({id:4,name:'中山',pId:2,cIds:-1});
	    d.push({id:5,name:'宁波',pId:2,cIds:-1});
	    d.push({id:6,name:'佛山',pId:2,cIds:-1});
	    d.push({id:7,name:'东莞',pId:2,cIds:-1});
	    d.push({id:8,name:'泵/阀',pId:1,cIds:3});
	    d.push({id:9,name:'永嘉',pId:8,cIds:-1});
	    d.push({id:10,name:'温岭',pId:8,cIds:-1});
	    d.push({id:11,name:'玉环',pId:8,cIds:-1});
	    d.push({id:12,name:'模具',pId:1,cIds:4});
	    d.push({id:13,name:'黄岩',pId:12,cIds:-1});
	    d.push({id:14,name:'宁波',pId:12,cIds:-1});
	    d.push({id:15,name:'上海',pId:12,cIds:-1});
	    d.push({id:16,name:'深圳',pId:12,cIds:-1});
	    d.push({id:17,name:'机械',pId:1,cIds:5});
	    d.push({id:18,name:'温州',pId:17,cIds:-1});
	    d.push({id:19,name:'宁波',pId:17,cIds:-1});
	    d.push({id:20,name:'泰州',pId:17,cIds:-1});
	    d.push({id:21,name:'电子、电工',pId:0,cIds:6});
	    d.push({id:22,name:'灯具',pId:21,cIds:7});
	    d.push({id:23,name:'中山',pId:22,cIds:-1});
	    d.push({id:24,name:'余姚',pId:22,cIds:-1});
	    d.push({id:25,name:'上虞',pId:22,cIds:-1});
	    d.push({id:26,name:'武进',pId:22,cIds:-1});
	    d.push({id:27,name:'宁海',pId:22,cIds:-1});
	    d.push({id:28,name:'磁性材料',pId:21,cIds:8});
	    d.push({id:29,name:'东阳',pId:28,cIds:-1});
	    d.push({id:30,name:'广州',pId:28,cIds:-1});
	    d.push({id:31,name:'集成电路',pId:21,cIds:9});
	    d.push({id:32,name:'深圳',pId:31,cIds:-1});
	    d.push({id:33,name:'广州',pId:31,cIds:-1});
	    d.push({id:34,name:'广东',pId:31,cIds:-1});
	    d.push({id:35,name:'电子',pId:21,cIds:10});
	    d.push({id:36,name:'吴江',pId:35,cIds:-1});
	    d.push({id:37,name:'中山',pId:35,cIds:-1});
	    d.push({id:38,name:'无锡',pId:35,cIds:-1});
	    d.push({id:39,name:'上海',pId:35,cIds:-1});
	    d.push({id:40,name:'广东',pId:35,cIds:-1});
	    d.push({id:41,name:'电气',pId:21,cIds:11});
	    d.push({id:42,name:'温州',pId:41,cIds:-1});
	    d.push({id:43,name:'上海',pId:41,cIds:-1});
	    d.push({id:44,name:'北京',pId:41,cIds:-1});
	    d.push({id:45,name:'宁波',pId:41,cIds:-1});
	    d.push({id:46,name:'广东',pId:41,cIds:-1});
	    d.push({id:47,name:'冶金',pId:0,cIds:12});
	    d.push({id:48,name:'钢材',pId:47,cIds:13});
	    d.push({id:49,name:'北京',pId:48,cIds:-1});
	    d.push({id:50,name:'上海',pId:48,cIds:-1});
	    d.push({id:51,name:'广州',pId:48,cIds:-1});
	    d.push({id:52,name:'杭州',pId:48,cIds:-1});
	    d.push({id:53,name:'南京',pId:48,cIds:-1});
	    d.push({id:54,name:'有色金属',pId:47,cIds:14});
	    d.push({id:55,name:'上海',pId:54,cIds:-1});
	    d.push({id:56,name:'广东',pId:54,cIds:-1});
	    d.push({id:57,name:'北京',pId:54,cIds:-1});
	    d.push({id:58,name:'天津',pId:54,cIds:-1});
	    d.push({id:59,name:'无锡',pId:54,cIds:-1});
	    d.push({id:60,name:'不锈钢',pId:47,cIds:15});
	    d.push({id:61,name:'江苏',pId:60,cIds:-1});
	    d.push({id:62,name:'上海',pId:60,cIds:-1});
	    d.push({id:63,name:'浙江',pId:60,cIds:-1});
	    d.push({id:64,name:'广东',pId:60,cIds:-1});
	    d.push({id:65,name:'管材',pId:47,cIds:16});
	    d.push({id:66,name:'上海',pId:65,cIds:-1});
	    d.push({id:67,name:'佛山',pId:65,cIds:-1});
	    d.push({id:68,name:'河北',pId:65,cIds:-1});
	    d.push({id:69,name:'聊城',pId:65,cIds:-1});
	    d.push({id:70,name:'无锡',pId:65,cIds:-1});
	    d.push({id:71,name:'建材',pId:47,cIds:17});
	    d.push({id:72,name:'上海',pId:71,cIds:-1});
	    d.push({id:73,name:'江苏',pId:71,cIds:-1});
	    d.push({id:74,name:'广东',pId:71,cIds:-1});
	    d.push({id:75,name:'天津',pId:71,cIds:-1});
	    d.push({id:76,name:'邯郸',pId:71,cIds:-1});
	    d.push({id:77,name:'服装、鞋帽',pId:0,cIds:18});
	    d.push({id:78,name:'服装',pId:77,cIds:19});
	    d.push({id:79,name:'宁波',pId:78,cIds:-1});
	    d.push({id:80,name:'嘉兴',pId:78,cIds:-1});
	    d.push({id:81,name:'杭州',pId:78,cIds:-1});
	    d.push({id:82,name:'广州',pId:78,cIds:-1});
	    d.push({id:83,name:'服装',pId:77,cIds:20});
	    d.push({id:84,name:'潮州',pId:83,cIds:-1});
	    d.push({id:85,name:'深圳',pId:83,cIds:-1});
	    d.push({id:86,name:'石狮',pId:83,cIds:-1});
	    d.push({id:87,name:'虎门',pId:83,cIds:-1});
	    d.push({id:88,name:'服装',pId:77,cIds:21});
	    d.push({id:89,name:'中山',pId:88,cIds:-1});
	    d.push({id:90,name:'常熟',pId:88,cIds:-1});
	    d.push({id:91,name:'绍兴',pId:88,cIds:-1});
	    d.push({id:92,name:'鞋',pId:77,cIds:22});
	    d.push({id:93,name:'温州',pId:92,cIds:-1});
	    d.push({id:94,name:'晋江',pId:92,cIds:-1});
	    d.push({id:95,name:'南通',pId:92,cIds:-1});
	    d.push({id:96,name:'内衣',pId:77,cIds:23});
	    d.push({id:97,name:'义乌',pId:96,cIds:-1});
	    d.push({id:98,name:'汕头',pId:96,cIds:-1});
	    d.push({id:99,name:'纺织、皮革',pId:0,cIds:24});
	    d.push({id:100,name:'纺织',pId:99,cIds:25});
	    d.push({id:101,name:'张家港',pId:100,cIds:-1});
	    d.push({id:102,name:'通州',pId:100,cIds:-1});
	    d.push({id:103,name:'江阴',pId:100,cIds:-1});
	    d.push({id:104,name:'太仓',pId:100,cIds:-1});
	    d.push({id:105,name:'面料',pId:99,cIds:26});
	    d.push({id:106,name:'绍兴',pId:105,cIds:-1});
	    d.push({id:107,name:'湖州',pId:105,cIds:-1});
	    d.push({id:108,name:'萧山',pId:105,cIds:-1});
	    d.push({id:109,name:'吴江',pId:105,cIds:-1});
	    d.push({id:110,name:'面料',pId:99,cIds:27});
	    d.push({id:111,name:'常熟',pId:110,cIds:-1});
	    d.push({id:112,name:'海宁',pId:110,cIds:-1});
	    d.push({id:113,name:'江门',pId:110,cIds:-1});
	    d.push({id:114,name:'南通',pId:110,cIds:-1});
	    d.push({id:115,name:'潍坊',pId:110,cIds:-1});
	    d.push({id:116,name:'辅料',pId:99,cIds:28});
	    d.push({id:117,name:'义乌',pId:116,cIds:-1});
	    d.push({id:118,name:'汕头',pId:116,cIds:-1});
	    d.push({id:119,name:'东莞',pId:116,cIds:-1});
	    d.push({id:120,name:'广州',pId:116,cIds:-1});
	    d.push({id:121,name:'皮革',pId:99,cIds:29});
	    d.push({id:122,name:'海宁',pId:121,cIds:-1});
	    d.push({id:123,name:'桐乡',pId:121,cIds:-1});
	    d.push({id:124,name:'花都',pId:121,cIds:-1});
	    d.push({id:125,name:'家居用品',pId:0,cIds:30});
	    d.push({id:126,name:'家具',pId:125,cIds:31});
	    d.push({id:127,name:'东莞',pId:126,cIds:-1});
	    d.push({id:128,name:'深圳',pId:126,cIds:-1});
	    d.push({id:129,name:'中山',pId:126,cIds:-1});
	    d.push({id:130,name:'安吉',pId:126,cIds:-1});
	    d.push({id:131,name:'陶瓷',pId:125,cIds:32});
	    d.push({id:132,name:'潮州',pId:131,cIds:-1});
	    d.push({id:133,name:'宜兴',pId:131,cIds:-1});
	    d.push({id:134,name:'淄博',pId:131,cIds:-1});
	    d.push({id:135,name:'眼镜',pId:125,cIds:33});
	    d.push({id:136,name:'温州',pId:135,cIds:-1});
	    d.push({id:137,name:'丹阳',pId:135,cIds:-1});
	    d.push({id:138,name:'皮包',pId:125,cIds:34});
	    d.push({id:139,name:'义乌',pId:138,cIds:-1});
	    d.push({id:140,name:'广州',pId:138,cIds:-1});
	    d.push({id:141,name:'家用塑料制品',pId:125,cIds:35});
	    d.push({id:142,name:'义乌',pId:141,cIds:-1});
	    d.push({id:143,name:'包装、印刷',pId:0,cIds:36});
	    d.push({id:144,name:'包装',pId:143,cIds:37});
	    d.push({id:145,name:'深圳',pId:144,cIds:-1});
	    d.push({id:146,name:'东莞',pId:144,cIds:-1});
	    d.push({id:147,name:'广州',pId:144,cIds:-1});
	    d.push({id:148,name:'上海',pId:144,cIds:-1});
	    d.push({id:149,name:'温州',pId:144,cIds:-1});
	    d.push({id:150,name:'印刷',pId:143,cIds:38});
	    d.push({id:151,name:'上海',pId:150,cIds:-1});
	    d.push({id:152,name:'广东',pId:150,cIds:-1});
	    d.push({id:153,name:'浙江',pId:150,cIds:-1});
	    d.push({id:154,name:'北京',pId:150,cIds:-1});
	    d.push({id:155,name:'江苏',pId:150,cIds:-1});
	    d.push({id:156,name:'塑料包装',pId:143,cIds:39});
	    d.push({id:157,name:'江浙沪',pId:156,cIds:-1});
	    d.push({id:158,name:'广东',pId:156,cIds:-1});
	    d.push({id:159,name:'山东',pId:156,cIds:-1});
	    d.push({id:160,name:'纸业',pId:143,cIds:40});
	    d.push({id:161,name:'深圳',pId:160,cIds:-1});
	    d.push({id:162,name:'广州',pId:160,cIds:-1});
	    d.push({id:163,name:'东莞',pId:160,cIds:-1});
	    d.push({id:164,name:'浙江',pId:160,cIds:-1});
	    d.push({id:165,name:'上海',pId:160,cIds:-1});
	    d.push({id:166,name:'包装印刷',pId:143,cIds:41});
	    d.push({id:167,name:'上海',pId:166,cIds:-1});
	    d.push({id:168,name:'北京',pId:166,cIds:-1});
	    d.push({id:169,name:'广东',pId:166,cIds:-1});
	    d.push({id:170,name:'浙江',pId:166,cIds:-1});
	    d.push({id:171,name:'化工',pId:0,cIds:42});
	    d.push({id:172,name:'塑料',pId:171,cIds:43});
	    d.push({id:173,name:'余姚',pId:172,cIds:-1});
	    d.push({id:174,name:'齐鲁',pId:172,cIds:-1});
	    d.push({id:175,name:'台州',pId:172,cIds:-1});
	    d.push({id:176,name:'东莞',pId:172,cIds:-1});
	    d.push({id:177,name:'常州',pId:172,cIds:-1});
	    d.push({id:178,name:'塑料',pId:171,cIds:44});
	    d.push({id:179,name:'厦门',pId:178,cIds:-1});
	    d.push({id:180,name:'顺德',pId:178,cIds:-1});
	    d.push({id:181,name:'天津',pId:178,cIds:-1});
	    d.push({id:182,name:'临沂',pId:178,cIds:-1});
	    d.push({id:183,name:'化工',pId:171,cIds:45});
	    d.push({id:184,name:'上海',pId:183,cIds:-1});
	    d.push({id:185,name:'广州',pId:183,cIds:-1});
	    d.push({id:186,name:'宁波',pId:183,cIds:-1});
	    d.push({id:187,name:'淄博',pId:183,cIds:-1});
	    d.push({id:188,name:'化工',pId:171,cIds:46});
	    d.push({id:189,name:'张家港',pId:188,cIds:-1});
	    d.push({id:190,name:'东北',pId:188,cIds:-1});
	    d.push({id:191,name:'华北',pId:188,cIds:-1});
	    d.push({id:192,name:'华南',pId:188,cIds:-1});
	    d.push({id:193,name:'染料',pId:171,cIds:47});
	    d.push({id:194,name:'绍兴',pId:193,cIds:-1});
	    d.push({id:195,name:'杭州',pId:193,cIds:-1});
	    d.push({id:196,name:'橡塑',pId:171,cIds:48});
	    d.push({id:197,name:'青岛',pId:196,cIds:-1});
	    d.push({id:198,name:'衡水',pId:196,cIds:-1});
	    d.push({id:199,name:'海南',pId:196,cIds:-1});
	    return d;
	}
})(jQuery);  