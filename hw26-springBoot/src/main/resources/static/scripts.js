$(document).ready(function () {

    $('.update-contacts').on('click', 'button[data-update-contacts-url]', function () {
        let url = $(this).data('update-contacts-url');

        // adding the row index, needed when deleting a contact
        let formData = $('form').serializeArray();
        let param = {};
        param["name"] = $(this).attr('name');
        param["value"] = $(this).val();
        formData.push(param);

        // updating the contact section
        $('#tblPhones').load(url, formData);
    });
});