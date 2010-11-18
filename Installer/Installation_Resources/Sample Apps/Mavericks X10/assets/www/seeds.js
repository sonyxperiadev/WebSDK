// JavaScript Document/*

/* Define the number of seeds to be used in the animation */
const NUMBER_OF_SEEDS = 30;

function init()
{
    /* Get a reference to the element that will contain the seeds */
    var container = document.getElementById('seedContainer');
    /* Fill the empty container with new seeds */
    for (var i = 0; i < NUMBER_OF_SEEDS; i++) 
    {
        container.appendChild(createAseed());
    }
}

/*  Receives the lowest and highest values of a range and
    returns a random integer that falls within that range.
*/
function randomInteger(low, high)
{
    return low + Math.floor(Math.random() * (high - low));
}

/* Receives the lowest and highest values of a range and
   returns a random float that falls within that range.
*/
function randomFloat(low, high)
{
    return low + Math.random() * (high - low);
}

/*
    Receives a number and returns its CSS pixel value.
*/
function pixelValue(value)
{
    return value + 'px';
}

/*
    Returns a duration value for the falling animation.
*/

function durationValue(value)
{
    return value + 's';
}

/*
    Uses an img element to create each seed. "seeds.css" implements two spin 
    animations for the seeds: clockwiseSpin and counterclockwiseSpinAndFlip. This
    function determines which of these spin animations should be applied to each seed.
    
*/
function createAseed()
{
    /* Start by creating a wrapper div, and an empty img element */
    var seedDiv = document.createElement('div');
    var image = document.createElement('img');
    
    /* Randomly choose a seed image and assign it to the newly created element */
    image.src = 'imgs/seed' + randomInteger(1, 5) + '.png';
    
    seedDiv.style.left = "-200px";

    /* Position the seed at a random location along the screen */
    seedDiv.style.top = pixelValue(randomInteger(0, 250));
    
    /* Randomly choose a spin animation */
    var spinAnimationName = (Math.random() < 0.5) ? 'clockwiseSpin' : 'counterclockwiseSpinAndFlip';
    
    /* Set the -webkit-animation-name property with these values */
    seedDiv.style.webkitAnimationName = 'fade, drop';
    image.style.webkitAnimationName = spinAnimationName;
    
    /* Figure out a random duration for the fade and drop animations */
    var fadeAndDropDuration = durationValue(randomFloat(5, 11));
    
    /* Figure out another random duration for the spin animation */
    var spinDuration = durationValue(randomFloat(4, 8));
    /* Set the -webkit-animation-duration property with these values */
    seedDiv.style.webkitAnimationDuration = fadeAndDropDuration + ', ' + fadeAndDropDuration;

    var seedDelay = durationValue(randomFloat(0, 5));
    seedDiv.style.webkitAnimationDelay = seedDelay + ', ' + seedDelay;

    image.style.webkitAnimationDuration = spinDuration;

    // add the <img> to the <div>
    seedDiv.appendChild(image);

    /* Return this img element so it can be added to the document */
    return seedDiv;
}

/* Calls the init function when the "Falling seeds" page is full loaded */
window.addEventListener('load', init, false);