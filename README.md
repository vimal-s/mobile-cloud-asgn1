# Assignment 1

## Running the Application

To run the application:

Right-click on the Application class in the org.magnum.dataup
package, Run As->Java Application

## Overview

A popular use of cloud services is to manage media that is uploaded
from mobile devices. This assignment will create a very basic application
for uploading video to a cloud service and managing the video's metadata.
Once you are able to build this basic type of infrastructure, you will have
the core knowledge needed to create much more sophisticated cloud services.

## Instructions

This assignment tests your ability to create a web application that
allows clients to upload videos to a server. The server allows clients
to first upload a video's metadata (e.g., duration, etc.) and then to
upload the actual binary data for the video. The server should support
uploading video binary data with a multipart request. 

The HTTP API implemented is as follows:
 
GET /video
   - Returns the list of videos that have been added to the server as JSON. 
     The list of videos does not persisted across restarts of the server.
     
POST /video
   - The video metadata is provided as an application/json request body.
     The JSON should generate a valid instance of the Video class when deserialized by Spring's default 
     Jackson library.
   - Returns the JSON representation of the Video object that was stored along with any updates to that object made by the server. 
   - **_The server should generate a unique identifier for the Video object and assign it to the Video by calling its setId(...)
     method._** 
   - The returned Video JSON should include this server-generated identifier so that the client can refer to it when uploading the
     binary mpeg video content for the Video.
   - The server should also generate a "data url" for the Video. The "data url" is the url of the binary data for a
     Video (e.g., the raw mpeg data).
     The URL should be the _full_ URL for the video and not just the path (e.g., http://localhost:8080/video/1/data would
     be a valid data url).
     
POST /video/{id}/data
   - The binary mpeg data for the video should be provided in a multipart request as a part with the key "data".
     The id in the path should be replaced with the unique identifier generated by the server for the Video.
     A client MUST *create* a Video first by sending a POST to /video and getting the identifier for the newly created Video object before
     sending a POST to /video/{id}/data.
   - The endpoint should returns a VideoStatus object with state=VideoState.READY if the request succeeds and the appropriate HTTP error        
     status otherwise.
     
GET /video/{id}/data
   - Returns the binary mpeg data (if any) for the video with the given identifier. 
     If no mpeg data has been uploaded for the specified video, then the server returns a 404 status code.
 
 No code is modified in Video, VideoStatus, VideoSvcApi, AutoGrading, or AutoGradingTest.
