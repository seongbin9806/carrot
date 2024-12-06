const util = {
    postLocation(url, param, target = '') {
        let f = document.createElement('form');
        let objs, value;
        
        for (let key in param) {
            value = param[key];
            objs = document.createElement('input');
            objs.setAttribute('type', 'hidden');
            objs.setAttribute('name', key);
            objs.setAttribute('value', value);
            f.appendChild(objs)
        }    
        if (target) {
            f.setAttribute('target', target)
        }

        f.setAttribute('method', 'post');
        f.setAttribute('action', url);
        document.body.appendChild(f);
        f.submit();
    },
    showLoadingBar: function() {
//        document.getElementById('mask').style.display = 'block';
        document.getElementById('loadingImg').style.display = 'block';
    },
    hideLoadingBar: function() {
//        document.getElementById('mask').style.display = 'none';
        document.getElementById('loadingImg').style.display = 'none';
    },
    fetchJsonData: function(url, data, isLoading = true, method = 'POST') {
//        if (isLoading) this.showLoadingBar();
        
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'
                },
                body: new URLSearchParams(data).toString()
            })
            .then(response => response.json())
            .then(data => {
                resolve(data);
//                if (isLoading) this.hideLoadingBar();
            })
            .catch(error => {
                alert("Error: " + error.message);
//                if (isLoading) this.hideLoadingBar();
                reject(error);
            });
        });
    },
    fetchArrJsonData: function(url, data, isLoading = true, method = 'POST') {
        if (isLoading) this.showLoadingBar();
        
        return new Promise((resolve, reject) => {
            fetch(url, {
                method: method,
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(data)
            })
            .then(response => response.json())
            .then(data => {
                resolve(data);
                if (isLoading) this.hideLoadingBar();
            })
            .catch(error => {
                alert("Error: " + error.message);
                if (isLoading) this.hideLoadingBar();
                reject(error);
            });
        });
    },
    showAlert: function(message, destroyEvent = '') {
        return swal.fire({
            html: message,
            confirmButtonText: '확인',
            didDestroy: () => {
                if (typeof destroyEvent === 'function') { // Check if destroyEvent is a function
                    destroyEvent();
                }
            }
        });
    },
    showConfirm: function(message) {
        return Swal.fire({
            html: message,
            confirmButtonText: '확인',
            denyButtonText: '취소',
            showDenyButton: true
        });
    },
    /* toast메세지 */
    showToastMsg(msg = "", type = 'W'){
        let $toast = $('#toast'),
            backgroundColor = "";
                
        switch(type){
            case 'W' : /* warning */
                backgroundColor = '#FF4C4C';
            break;
            case 'S' : /* success */
                backgroundColor = '#333';
            break;
        }
        
        $toast.html(msg)
            .css('display', 'block')
            .css('background', backgroundColor)
            .animate({ top: '20px' }, 300, function() {
            // Wait for 4 seconds
            setTimeout(function() {
                // Hide toast by animating it out of view
                $toast.animate({ top: '-100px' }, 300, function() {
                    //$toast.css('display', 'none');
                });
            }, 3000);
        });
    },
    setCookie: function(name, value, days = 30) {
        let expires = "";
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "") + expires + "; path=/";
    },
    deleteCookie: function(name) {
        document.cookie = name + "=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    },
    getCookie: function(name) {
        var nameEQ = name + "=";
        var cookies = document.cookie.split(';');
        for(var i = 0; i < cookies.length; i++) {
            var cookie = cookies[i];
            while (cookie.charAt(0) === ' ') {
                cookie = cookie.substring(1, cookie.length);
            }
            if (cookie.indexOf(nameEQ) === 0) {
                return cookie.substring(nameEQ.length, cookie.length);
            }
        }
        return null;
    },
    nl2br: function(str){
        return str.replace(/(\r\n|\n|\r)/g, '<br>');
    }
}