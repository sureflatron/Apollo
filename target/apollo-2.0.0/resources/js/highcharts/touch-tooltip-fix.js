Highcharts.Chart.prototype.callbacks.push(function(chart) {
	var hasTouch = document.documentElement.ontouchstart !== undefined, mouseTracker = chart.pointer, container = chart.container, mouseMove;

	mouseMove = function(e) {
		if (hasTouch) {
			if (e && e.touches && e.touches.length > 1) {
				mouseTracker.onContainerTouchMove(e);
			} else {
				return;
			}
		} else {
			mouseTracker.onContainerMouseMove(e);
		}
	};

	click = function(e) {
		if (hasTouch) {
			mouseTracker.onContainerMouseMove(e);
		}
		mouseTracker.onContainerClick(e);
	}

	container.onmousemove = container.ontouchstart = container.ontouchmove = mouseMove;
	container.onclick = click;
});