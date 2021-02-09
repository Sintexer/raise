

function deleteQuestion(id, url) {
    fetch(url+"?commentId="+id, {
        method: 'POST',
    }).then(() => location.reload());
}