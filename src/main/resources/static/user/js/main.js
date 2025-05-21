(function () {
    'use strict';

    $(function () {
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
        $(".payment-checkbox:checked").map(function () {
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
                bookingId: booking_id,
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

    $(document).on("click", "#btnSubmitBooking", function (e) {
        setDataInitialize();
        let isDuplicate = checkSameSeat();
        if (isDuplicate) {
            alert('1 em bé chỉ được phép ngồi cùng 1 người lớn!!!!');
            return;
        }
        let formId = $(this).data("form_id");
        $.ajax({
            method: $(formId).attr('method'),
            url: $(formId).attr('action'),
            data: $(formId).serialize(),
            dataType: 'json'
        }).done(function (data) {
            // $("#modalResult .modal-body").text(data.message);
            $("#modalResult .link-seat-select").attr("href", data.url);
            $("#modalResult").modal("show");
            setTimeout(function () {
                window.location.href = data.url;
            }, 9000)
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                if (jqXhr.responseJSON.message == 'Validation failed') {
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
        let total = parseInt($("input[name='ticket_total']").val());
        $(".luggage-select").each(function (index, element) {
            let $element = $(element);
            let kg = $element.val();
            if (kg != "" && kg != 0) {
                let price = kg * PRICE_FOR_A_KG;
                total += parseInt(price);
                $("input[name='totalPrice']").val(total);
            }
        });
    });


    $(document).on("click", "#btnTicketDetail", function (e) {
        // $("#ticketDetail").modal("show");
        const ticketId = $(this).data("id");
        const url = `/api/ticket/${ticketId}`;
        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json'
        }).done(function (data) {
            if (data.html) {
                $("#ticketDetail .modal-body").html(data.html);
                $("#ticketDetail").modal("show");
            }
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });
    });

    $(document).on("click", "#btnTicketDetailArrival", function (e) {
        const bookingId = $(this).data("id");
        const flightId = $(this).data("flight-id");
        console.log(bookingId)
        console.log(flightId)
        const url = `/api/ticket/${bookingId}/${flightId}`;
        console.log(url)

        $.ajax({
            type: 'GET',
            url: url,
            dataType: 'json'
        }).done(function (data) {
            if (data.html) {
                $("#ticketDetail .modal-body").html(data.html);
                $("#ticketDetail").modal("show");
            }
        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXhr.responseJSON.errors) {
                alert(jqXhr.responseJSON.message);
            }
        });
    });


    $(document).on("click", "#btnSendEmailTicket", function (e) {
        const code = $(this).data("id");
        const url = `/api/ticket/send-email/${code}`;
        $("#loadingSendEmail").removeClass("d-none").addClass("d-flex");

        $.ajax({
            type: 'GET',
            url: url,
        }).done(function (data, textStatus, jqXHR) {
            if (jqXHR.status === 200) {
                $("#loadingSendEmail").removeClass("d-flex").addClass("d-none");
                $("#ticketDetail").modal("hide");
                $("#sendEmailSuccess").modal("show");
            }

        }).fail(function (jqXhr, json, errorThrown) {
            if (jqXHR.status !== 200) {
                $("#sendEmailError").modal("show");
            }
        });
    });

    $(document).on("click", "#exportTicket", function (e) {
        const code = $(this).data("id");
        const url = `/api/ticket/export-ticket/${code}`;

        $.ajax({
            type: 'GET',
            url: url,
            xhrFields: {
                responseType: 'blob'
            },
            success: function (data, textStatus, jqXHR) {
                const blob = new Blob([data], {type: 'application/pdf'});

                const disposition = jqXHR.getResponseHeader('Content-Disposition');
                let fileName = "ticket.pdf";
                if (disposition && disposition.indexOf("filename=") !== -1) {
                    fileName = disposition.split("filename=")[1].replace(/"/g, "");
                }

                const downloadUrl = URL.createObjectURL(blob);
                const a = document.createElement('a');
                a.href = downloadUrl;
                a.download = fileName;
                document.body.appendChild(a);
                a.click();
                a.remove();

                URL.revokeObjectURL(downloadUrl);
            },
            error: function (jqXhr, textStatus, errorThrown) {
                alert("Không thể tải file PDF.");
                console.error(errorThrown);
            }
        });
    });


})();

function setDataInitialize() {
    $('.invalid-feedback strong').text('');
    $('div.invalid-feedback').text('');
    $('.text-danger strong').text('');
    $('input').removeClass('is-invalid');
    $('textarea').removeClass('is-invalid');
}

function checkSameSeat() {
    let values = [];
    let isDuplicate = false;

    $('.select-index-seat').each(function () {
        let val = $(this).val();
        if (val) {
            if (values.includes(val)) {
                isDuplicate = true;
                return false; // thoát khỏi vòng lặp
            }
            values.push(val);
        }
    });

    return isDuplicate;
}
