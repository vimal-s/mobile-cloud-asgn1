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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.Headers;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class RequestController implements VideoSvcApi {

    private List<Video> videos = new ArrayList<>();

//    private String getUrlBaseForLocalServer() {
//        HttpServletRequest request =
//                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
//                        .getRequest();
//        String base = "http://" + request.getServerName() +
//                ((request.getServerPort() != 80) ? ":" + request.getServerPort() : "");
//        return base;
//    }

    @Override
    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.GET)
    @ResponseBody
    public List<Video> getVideoList() {
        System.out.println("Inside getVideoList()");
        return videos;
    }

    @RequestMapping(value = VIDEO_SVC_PATH, method = RequestMethod.POST)
    public @ResponseBody Video addVideo(@RequestBody Video v) {
        System.out.println("Inside addVideo()");
        boolean isPresent = false;
        int index = -1;
        for (Video video : videos) {
            index++;
            if (video.getId() == v.getId()) {
                isPresent = true;
                break;
            }
        }
        if (!isPresent) {
            index++;
            long id = videos.size() + 1;
            v.setId(id);
            videos.add(v);
        }
        return videos.get(index);
    }

    @Override
    public VideoStatus setVideoData(long id, TypedFile videoData) {
        return null;
    }

    @Override
    public Response getData(long id) {
        return null;
    }

//    public void start() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(getUrlBaseForLocalServer())
//                .build();
//
//        VideoSvcApi videoSvcApi = retrofit.create(VideoSvcApi.class);
//
//        Call<Collection<Video>> videos = videoSvcApi.getVideoList();
//        call.enqueue(this);
//    }
}
