$(document).on("click", "#btnUpdate", function (e) {
    const id = $(this).data("id");
    const url = `/admin/customer/update/${id}`;
    $.ajax({
        type: 'GET',
        url: url,
        dataType: 'json'
    }).done(function (data) {
        if (data.html) {
            $("#userDetail .modal-body").html(data.html);
            $("#userDetail").modal("show");
        }
    }).fail(function (jqXhr, json, errorThrown) {
        if (jqXhr.responseJSON.errors) {
            alert(jqXhr.responseJSON.message);
        }
    });
});

function previewImage(event) {
    const img = document.getElementById('previewImage');
    img.src = URL.createObjectURL(event.target.files[0]);
    img.style.display = 'block';
}

document.querySelector('input[name="imageFile"]').addEventListener('change', previewImage);