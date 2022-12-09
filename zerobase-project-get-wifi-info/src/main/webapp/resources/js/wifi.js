const lat_input = document.querySelector('#lat-input');
const lnt_input = document.querySelector('#lnt-input');
const get_position_btn = document.querySelector('#get-position-btn');
const get_wifi_btn = document.querySelector('#get-wifi-btn');

get_position_btn.addEventListener('click', (e) => {
	if (navigator.geolocation) {
		navigator.geolocation.getCurrentPosition(success, faild);
	} else {
		alert ("이 브라우저에서는 위치를 구할 수 없습니다")
	}
});

function success(pos) {
	let lat = pos.coords.latitude;
	let lnt = pos.coords.longitude;
	lat_input.value = lat;
	lnt_input.value = lnt;
}

function faild (e) {
	alert (e.code + e.message);
}

get_wifi_btn.addEventListener('click', function() { // function() 대신 (e) => 도 가능
	let lat_val = lat_input.value;
	let lnt_val = lnt_input.value;
	if (lat_val == "" || lnt_val == "") {
		alert("위치를 입력한 후에 조회해주세요! 현재 값이 비어있습니다.");
	} else if (isNaN(lat_val) || isNaN(lnt_val)) {
		alert("값이 바르지 않습니다. 다시 입력해주세요.");
	} else window.location.assign('/WIFI/get-wifi?lat=' + lat_val + "&lnt=" + lnt_val);
});