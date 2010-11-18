// MovieClip takes a series of images an animates them frame by frame like an animated GIF.

MovieClip = new Class({

	Implements: [Events, Options],

	options: {
		frames:			Array(),
		interval: 		1000,
		loop:			true
	},
	
	currentFrame: 0,
	loadedFrames: Array(),
	container: {},

	initialize: function(container, options) {
		this.setOptions(options);
		this.container = $(container);
		
		var that = this;
		var animation = new Asset.images(this.options.frames, {
			onComplete: function() {
				// add loaded images to container
				animation.each(function(frame, i) {
					that.container.adopt(frame);
					that.loadedFrames = that.container.getElements("img");
					if (i > 0) {
						that.loadedFrames[i].set("styles", {"display":"none"});
					}
				});
				
				// start animating
				that.advance.periodical(that.options.interval, that);
			}
		});
	},
	
	advance: function() {
		if (this.currentFrame < (this.options.frames.length - 1)) {
			// show next frame
			this.loadedFrames[this.currentFrame].set("styles", {"display":"none"});
			this.currentFrame++;
			this.loadedFrames[this.currentFrame].set("styles", {"display":"block"});
		} else if (this.options.loop == true) {
			// start at first frame
			this.loadedFrames[this.currentFrame].set("styles", {"display":"none"});
			this.currentFrame = 0;
			this.loadedFrames[this.currentFrame].set("styles", {"display":"block"});
		}
	}
});