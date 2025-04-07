(function () {
    'use strict';

    $(function() {
        $('.flash_message').fadeOut(5000);
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

})();