// Modified from http://gist.github.com/189534
Request.Twitter = new Class({
	Extends: Request.JSONP,

	options: {
		linkify: true,
		url: 'http://twitter.com/statuses/user_timeline/{term}.json',
		data: {
			count: 20
		}
	},

	initialize: function(term, options) {
		this.parent(options);
		this.options.url = this.options.url.substitute({term: term});
	},

	success: function(data, script) {
		if (this.options.linkify) data.each(function(tweet){
			tweet.text = this.linkify(tweet.text);
		}, this);
		
		// keep subsequent calls newer
		if (data[0]) this.options.data.since_id = data[0].id;
		
		this.parent(data, script);
	},

	linkify: function(text) {
		// courtesy of Jeremy Parrish (rrish.org)
		return text.replace(/(https?:\/\/[\w\-:;?&=+.%#\/]+)/gi, '<a href="$1">$1</a>')
							.replace(/(^|\W)@(\w+)/g, '$1<a href="http://twitter.com/$2">@$2</a>')
							.replace(/(^|\W)#(\w+)/g, '$1#<a href="http://search.twitter.com/search?q=%23$2">$2</a>');
	}
});

// Modified from http://github.com/trek/thoughtbox/blob/master/js_relative_dates/src/relative_date.js
RelativeDate = new Class({
	text: "",
	
	initialize: function(original_date) {
		var now	= new Date();
		var later = new Date(original_date);
		var offset = later.getTime() - now.getTime();
		this.text = this.relativeTime(offset);
	},

	relativeTime: function(offset){
		var distanceInMinutes = (offset.abs() / 60000).round();
		if (distanceInMinutes == 0) 			{ return 'seconds ago'; }
		else if (distanceInMinutes < 2)			{ return 'about a minute ago'; }
		else if (distanceInMinutes < 45) 		{ return distanceInMinutes + ' minutes ago';}
		else if (distanceInMinutes < 90)		{ return 'about 1 hour ago';}
		else if (distanceInMinutes < 1440)		{ return 'about ' + (distanceInMinutes / 60).round() + ' hours ago'; }
		else if (distanceInMinutes < 2880)		{ return '1 day ago'; }
		else if (distanceInMinutes < 43200) 	{ return 'about ' + (distanceInMinutes / 1440).round() + ' days ago'; }
		else if (distanceInMinutes < 86400) 	{ return 'about a month ago' }
		else if (distanceInMinutes < 525600) 	{ return 'about ' + (distanceInMinutes / 43200).round() + ' months ago'; }
		else if (distanceInMinutes < 1051200)	{ return 'about a year ago';}
		else return 'over ' + (distanceInMinutes / 525600).round() + ' years ago';
	}
});