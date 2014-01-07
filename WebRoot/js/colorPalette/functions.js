
function getOffsetTop(elm) {
	var mOffsetTop = elm.offsetTop;
	var mOffsetParent = elm.offsetParent;
	while (mOffsetParent) {
		mOffsetTop += mOffsetParent.offsetTop;
		mOffsetParent = mOffsetParent.offsetParent;
	}
	return mOffsetTop;
}
function getOffsetLeft(elm) {
	var mOffsetLeft = elm.offsetLeft;
	var mOffsetParent = elm.offsetParent;
	while (mOffsetParent) {
		mOffsetLeft += mOffsetParent.offsetLeft;
		mOffsetParent = mOffsetParent.offsetParent;
	}
	return mOffsetLeft;
}
function setColor(color) {
	if (ColorImg.id == "FontColorShow" && color == "#") {
		color = "#000000";
	}
	if (ColorImg.id == "FontBgColorShow" && color == "#") {
		color = "#FFFFFF";
	}
	if (ColorValue) {
		//ColorValue.value = color.substr(1);
		ColorValue.value = color;
		ColorValue.style.backgroundColor = color;
	}
	if (ColorImg && color.length > 2) {
		//ColorImg.src = "rect.gif";
		//ColorImg.style.backgroundColor = color;
	} else if (color == "#") {
		//ColorImg.src = "rectNoColor.gif";
		//ColorValue.value = "";	 
	}
	document.getElementById("colorPalette").style.visibility = "hidden";
}
function getColorPalette(imgId, inputId) {
	var paletteLeft, paletteTop;
	var colorPalette = document.getElementById("colorPalette");
	ColorImg = document.getElementById(imgId);
	ColorValue = document.getElementById(inputId);
	if (colorPalette) {
		paletteLeft = getOffsetLeft(ColorImg);
		paletteTop = (getOffsetTop(ColorImg) + ColorImg.offsetHeight);
		if (paletteLeft + 150 > parseInt(document.body.clientWidth)) {
			paletteLeft = parseInt(event.clientX) - 260;
		}
		if (paletteTop > parseInt(document.body.clientHeight)) {
			paletteTop = parseInt(document.body.clientHeight) - 165;
		}
		colorPalette.style.left = paletteLeft + "px";
		colorPalette.style.top = paletteTop + "px";
		if (colorPalette.style.visibility == "hidden") {
			colorPalette.style.visibility = "visible";
		} else {
			colorPalette.style.visibility = "hidden";
		}
	}
}
function getColorPalette2(imgId, inputId) {
	var colorPalette = document.getElementById("colorPalette");
	ColorImg = document.getElementById(imgId);
	ColorValue = document.getElementById(inputId);
	if (colorPalette) {
		if (colorPalette.style.visibility == "hidden") {
			colorPalette.style.visibility = "visible";
		} else {
			colorPalette.style.visibility = "hidden"
		}
	}
}

