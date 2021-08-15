# Kasper
## Distributed Files Sharing System


    1.Run the Bootstrap Server
    2.Create a Local Driver
        - Create A Directory with drive-ID in ./Drive
          ex: ./Drive/dir1

    3. Run Mvn build
        mvn clean install
    4. cd /target
    5.Run the Kasper application with DdriveId environment variable
        Java -jar kasper.1.0.0.jar -DdriveId={value}

        ex : Java -jar kasper.1.0.0.jar -DdriveId=dir1

    6. Get the node Ip and Port from Application Logs

    7. Enter http://{ip}:{port}/ in the browser to access the GUI for the node

    8. Downloaded file can be found in ./Download

    9. Available file name List ./FileList.txt