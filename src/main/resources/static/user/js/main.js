(function () {
    'use strict';

    $(function() {
        $('.flash_message').fadeOut(5000);
    });

    const PRICE_FOR_A_KG = 10000;

    $(document).ready(function () {
        // Kiểm tra nếu biến message có giá trị thì hiển thị modal
        let message = $("#successModal .modal-body p").text().trim();
        if (message.length > 0) {
            $("#successModal").modal("show");
        }
    });

    $(document).ready(function () {
        // Kiểm tra nếu biến message có giá trị thì hiển thị modal
        let message = $("#successError .modal-body p").text().trim();
        if (message.length > 0) {
            $("#successError").modal("show");
        }
    });

    $(document).on("click", "#btnPayment", function (e) {
        let booking_id = [];
        let total = 0;
        $(".payment-checkbox:checked").map(function() {
            let id = $(this).attr('data-id')
            booking_id.push(id);
            let price = parseInt($('#price_' + id).text().replace(/[.,]/g, ''));
            total += price;
        }).get();
        if (booking_id.length == 0 && total == 0) {
            alert("Vui lòng chọn hóa đơn để thực hiện thanh toán");
            return;
        }
        $.ajax({
            type: 'POST',
            url: '/api/payment',
            data: {
                bookingId : booking_id,
                total: total
            },
            dataType: 'json'
        }).done(function (data) {
            console.log(data.paymentUrl);
            window.location.href = data.paymentUrl;
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });

    });

    $(document).on("click", "#btnSubmit", function (e) {
        setDataInitialize();
        let formId = $(this).data("form_id");
        $.ajax({
            method: $(formId).attr('method'),
            url: $(formId).attr('action'),
            data: $(formId).serialize(),
            dataType: 'json'
        }).done(function (data) {
            console.log(data);
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                if (jqXhr.responseJSON.message == 'Validation failed') {
                    console.log(jqXhr.responseJSON.validator)
                    $.each(jqXhr.responseJSON.validator, function (key, value) {
                        var element = 'input';
                        if (key.includes("gender") || key.includes("extraKg") || key.includes("nationality")) {
                            element = "select";
                        }
                        var input = formId + ' ' + element + '[name="' + key + '"]';
                        if (element == "input") {
                            $(input).addClass('is-invalid');
                        }
                        $(input + '+span strong').text(value);
                        return;
                    });
                } else {
                    alert(jqXhr.responseJSON.message);
                }
            }
        });

    });
    $(document).on("change", ".luggage-select", function (e) {
        let kg = $(this).val();
        console.log(kg);
        let total = parseInt($("input[name='totalPrice']").val());
        if (kg != "") {
            let price = kg * PRICE_FOR_A_KG;
            total += parseInt(price);
            $("input[name='totalPrice']").val(total);
        }
    });

})();
function setDataInitialize()
{
    $('.invalid-feedback strong').text('');
    $('div.invalid-feedback').text('');
    $('.text-danger strong').text('');
    $('input').removeClass('is-invalid');
    $('textarea').removeClass('is-invalid');
}
