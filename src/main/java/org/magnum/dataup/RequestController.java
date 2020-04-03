/*
 *
 * Copyright 2014 Jules White
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
public class RequestController {

    private static long currentId;
    private Map<Long, Video> videos = new HashMap<>();

    private String getDataUrl(long videoId) {
        return getUrlBaseForLocalServer() + "/video/" + videoId + "/data";
    }

    private String getDataPath(long videoId) {
        return "videos/video" + videoId + ".mpg";
    }

    private String getUrlBaseForLocalServer() {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return "http://" + request.getServerName() +
                ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
    }

    public Video save(Video entity) {
        checkAndSetId(entity);
        videos.put(entity.getId(), entity);
        return entity;
    }

    private void checkAndSetId(Video entity) {
        /*if (entity.getId() == 0) {
            entity.setId(++currentId);
        }*/

        entity.setId(++currentId);
    }

    @RequestMapping(value = "/video", method = RequestMethod.GET)
    @ResponseBody
    public Collection<Video> getVideoList() {
        return videos.values();
    }

    @RequestMapping(value = "/video", method = RequestMethod.POST)
    @ResponseBody
    public Video addVideo(@RequestBody Video v) {

        if (!videos.containsKey(v.getId())) {
            save(v);
            v.setDataUrl(getDataUrl(v.getId()));    // should be below save() because id is not yet generated
        }

        return videos.get(v.getId());
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<VideoStatus> setVideoData(@PathVariable long id,
                                                    @RequestParam(value = "data") MultipartFile videoData)
            throws IOException {
        VideoStatus videoStatus = null;

        if (videos.containsKey(id)) {
            VideoFileManager videoFileManager = VideoFileManager.get();
            videoFileManager.saveVideoData(videos.get(id), videoData.getInputStream());
            videoStatus = new VideoStatus(VideoStatus.VideoState.READY);

            return new ResponseEntity<>(videoStatus, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> getData(@PathVariable long id) {

        if (videos.containsKey(id)) {
            File file = new File(getDataPath(id));
            Resource resource = new FileSystemResource(file);
            return new ResponseEntity<>(resource, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
