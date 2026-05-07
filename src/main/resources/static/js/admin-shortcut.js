document.addEventListener('keydown', function(e) {
    if (e.ctrlKey && e.shiftKey && e.key === '}') {
        window.location.href = '/admin.html';
    }
});
