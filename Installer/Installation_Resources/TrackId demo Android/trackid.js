(function(){
    this.loadURL = function(url, callback, direct)
    {
        var jxhr = new jXHR();
        if(direct != true)
        {
            if(document.location.protocol == "file:")
            {
                url = url.replace('http://', 'http://fahrullin.info/trackid/xss3.php/');
            }
            else
            {
                url = url.replace('http://', 'http:xss3.php/');
            }
        }
        url += ((url.indexOf('?') < 0) ? '?' : '&');
        url += "callback=?";
        if (document.getElementById("loader"))
        {
            document.getElementById("loader").style.display = "inline";
        }
        jxhr.onreadystatechange = function(data)
        {
            if (jxhr.readyState === 4)
            {
                if (document.getElementById("loader"))
                {
                    document.getElementById("loader").style.display = "none";
                }
                setTimeout(function()
                {
                    callback(data);
                }, 0);
            }
        };
        jxhr.onerror = function(message, url)
        {
            throw new Error(message, url);
        }
        jxhr.open("GET", url);
        jxhr.send(null);
    };
})();
