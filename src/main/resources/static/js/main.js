'use strict';

var leaveForm = document.querySelector('#leave-form');
var leaveError = document.querySelector('#leave-error');
var leaveSuccess = document.querySelector('#leave-success');

var searchFileForm = document.querySelector('#searchFileForm');
var searchFileFormInput = document.querySelector('#searchFileFormInput');
var searchFileError = document.querySelector('#searchFileError');
var searchFileSuccess = document.querySelector('#searchFileSuccess');

var downloadFileForm = document.querySelector('#downloadFileForm');

var singleUploadForm = document.querySelector('#singleUploadForm');
var singleFileUploadInput = document.querySelector('#singleFileUploadInput');
var singleFileUploadError = document.querySelector('#singleFileUploadError');
var singleFileUploadSuccess = document.querySelector('#singleFileUploadSuccess');

var multipleUploadForm = document.querySelector('#multipleUploadForm');
var multipleFileUploadInput = document.querySelector('#multipleFileUploadInput');
var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');

var fileSearchResponse = null;


function searchFile(file) {
    var formData = new FormData();
    formData.append("fileName", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/kasper/v1/searchFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        fileSearchResponse = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            searchFileError.style.display = "none";
            searchFileSuccess.innerHTML = "<p>File search was successful!</p>";
            var content = '<fieldset><legend>Choose File</legend>';
            content += '<div><input type="radio" id="dowloadFile1" name="fileDownloadChoice" value="fileName"><label for="fileName">' + fileSearchResponse.fileName + '</label></div>';
            content += '<div><input type="radio" id="dowloadFile2" name="fileDownloadChoice" value="fileName"><label for="fileName">' + fileSearchResponse.fileName + '</label></div>';
            content += '<div><button type="submit" class="primary submit-btn">Download</button></div></fieldset>';
            downloadFileForm.innerHTML = content;
            searchFileSuccess.style.display = "block";
            downloadFileForm.style.display = "block";
        } else {
            searchFileSuccess.style.display = "none";
            downloadFileForm.style.display = "none";
            searchFileError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function downloadFile(fileName,ipAddress,portID) {
    var formData = new FormData();
    formData.append("fileName", fileName);
    formData.append("ipAddress", ipAddress);
    formData.append("portID", portID);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/kasper/v1/downloadFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        fileSearchResponse = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            searchFileError.style.display = "none";
            searchFileSuccess.innerHTML = "<p>File download was successful!</p>";
            searchFileSuccess.style.display = "block";
        } else {
            searchFileSuccess.style.display = "none";
            downloadFileForm.style.display = "none";
            searchFileError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function leave(){
    var formData = new FormData();
        formData.append("leave", "leave");

        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/kasper/v1/leave");

        xhr.onload = function() {
            console.log(xhr.responseText);
            leaveResponse = xhr.responseText;
            if(xhr.status == 200) {
              leaveSuccess.innerHTML = "<p>Leaving...!</p>";

            } else {

                leaveError.innerHTML = (response && response.message) || "Some Error Occurred";
            }
        }

        xhr.send(formData);
}

function uploadSingleFile(file) {
    var formData = new FormData();
    formData.append("file", file);

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadFile");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            singleFileUploadError.style.display = "none";
            singleFileUploadSuccess.innerHTML = "<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='" + response.fileDownloadUri + "' target='_blank'>" + response.fileDownloadUri + "</a></p>";
            singleFileUploadSuccess.style.display = "block";
        } else {
            singleFileUploadSuccess.style.display = "none";
            singleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function uploadMultipleFiles(files) {
    var formData = new FormData();
    for(var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/uploadMultipleFiles");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if(xhr.status == 200) {
            multipleFileUploadError.style.display = "none";
            var content = "<p>All Files Uploaded Successfully</p>";
            for(var i = 0; i < response.length; i++) {
                content += "<p>DownloadUrl : <a href='" + response[i].fileDownloadUri + "' target='_blank'>" + response[i].fileDownloadUri + "</a></p>";
            }
            multipleFileUploadSuccess.innerHTML = content;
            multipleFileUploadSuccess.style.display = "block";
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

leaveForm.addEventListener('submit', function(event){
   leave();
}, true);

searchFileForm.addEventListener('submit', function(event){
    var fileName = searchFileFormInput.value;
    if(fileName.length === 0) {
        searchFileError.innerHTML = "Please select a file name!";
        searchFileError.style.display = "block";
    }
    searchFile(fileName);
    event.preventDefault();
}, true);

downloadFileForm.addEventListener('submit', function(event){

    const rbs = document.querySelectorAll('input[name="fileDownloadChoice"]');
    let selectedValue;
    for (const rb of rbs) {
        if (rb.checked) {
            selectedValue = rb.value;
            break;
        }
    }

    alert(fileSearchResponse.fileName);

    downloadFile(fileSearchResponse.fileName, fileSearchResponse.ipAddress, fileSearchResponse.portId)

//    var fileName = searchFileFormInput.value;
//    if(fileName.length === 0) {
//        searchFileError.innerHTML = "Please select a file name!";
//        searchFileError.style.display = "block";
//    }
//    searchFile(fileName);
    event.preventDefault();
}, true);

singleUploadForm.addEventListener('submit', function(event){
    var files = singleFileUploadInput.files;
    if(files.length === 0) {
        singleFileUploadError.innerHTML = "Please select a file";
        singleFileUploadError.style.display = "block";
    }
    uploadSingleFile(files[0]);
    event.preventDefault();
}, true);


multipleUploadForm.addEventListener('submit', function(event){
    var files = multipleFileUploadInput.files;
    if(files.length === 0) {
        multipleFileUploadError.innerHTML = "Please select at least one file";
        multipleFileUploadError.style.display = "block";
    }
    uploadMultipleFiles(files);
    event.preventDefault();
}, true);


