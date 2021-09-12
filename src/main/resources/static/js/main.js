'use strict';

var leaveForm = document.querySelector('#leaveForm');
var leaveError = document.querySelector('#leaveError');
var leaveSuccess = document.querySelector('#leaveSuccess');

var searchFileForm = document.querySelector('#searchFileForm');
var searchFileFormInput = document.querySelector('#searchFileFormInput');
var searchFileError = document.querySelector('#searchFileError');
var searchFileSuccess = document.querySelector('#searchFileSuccess');

var downloadFileForm = document.querySelector('#downloadFileForm');

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
            searchFileSuccess.innerHTML = "<p>File search success!</p>";
            var content = '<fieldset class="file-choose-fieldset"><legend>Choose File</legend>';
            content += '<div><table id="files"><tr><th>Select</th><th>File Name</th><th>Ip Address</th><th>Port</th></tr>';
            for(var index=0; index < fileSearchResponse.files.length; index++){
                content += '<tr><td><input type="radio" id='+index+' name="fileDownloadChoice"></td><td>' + fileSearchResponse.files[index] + '</td><td>' + fileSearchResponse.ip + '</td><td>' + fileSearchResponse.port + '</td></tr>';
            }
            content += '</table><div><button type="submit" class="primary submit-btn">Download</button></div></fieldset>';
            downloadFileForm.innerHTML = content;
            searchFileSuccess.style.display = "block";
            downloadFileForm.style.display = "block";
        } else {
            searchFileSuccess.innerHTML = "<p>File Not Found!</p>";
            searchFileSuccess.style.display = "block";
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
            searchFileSuccess.style.display = "none";
            searchFileFormInput.value = "";
            downloadFileForm.style.display = "none";
            alert("File Downloaded Successfully!");
            gohome();
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
        formData.append("msg", "leave");

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

function uploadMultipleFiles(files) {
    var formData = new FormData();
    for(var index = 0; index < files.length; index++) {
        formData.append("files", files[index]);
    }

    var xhr = new XMLHttpRequest();
    xhr.open("POST", "/kasper/v1/uploadMultipleFiles");

    xhr.onload = function() {
        console.log(xhr.responseText);
        var response = xhr.responseText;
        if(xhr.status == 200) {
            multipleFileUploadError.style.display = "none";
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadInput.value = "";
            alert("Files Uploaded Successfully");
            gohome();
        } else {
            multipleFileUploadSuccess.style.display = "none";
            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
        }
    }

    xhr.send(formData);
}

function goBackToHome(){
    window.location.href="./index.html"
}

function gotohome(){
    searchFileError.style.display = "none";
    searchFileSuccess.style.display = "none";
    searchFileFormInput.value = "";
    downloadFileForm.style.display = "none";
    multipleFileUploadError.style.display = "none";
    multipleFileUploadSuccess.style.display = "none";
    multipleFileUploadInput.value = "";
    window.location.href="./index.html"
}

function gotoaboutus(){
    window.location.href="./about.html"
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
    let fileIndex;
    for (const rb of rbs) {
        if (rb.checked) {
            fileIndex = rb.id;
            break;
        }
    }
    downloadFile(fileSearchResponse.files[fileIndex], fileSearchResponse.ip, fileSearchResponse.port)
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


