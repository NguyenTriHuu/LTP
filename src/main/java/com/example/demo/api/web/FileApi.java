package com.example.demo.api.web;

import com.example.demo.Entity.FileEntity;
import com.example.demo.dto.ExerciseRequet;
import com.example.demo.service.ExerciseService;
import com.example.demo.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "api")
@RequiredArgsConstructor
@CrossOrigin
public class FileApi {
    private final FileService fileService;
    private final ExerciseService exerciseService;
   /* @PostMapping(value = "/course/lecture/{idLecture}/file/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addFiletoLecture(@PathVariable("idLecture") Long id, @ModelAttribute MultipartFile file){
        fileService.addFileToLecture(id,file);
    }*/

    @PostMapping(value = "/course/lecture/{idLecture}/file/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void Save(@PathVariable("idLecture") Long id, @RequestParam(value = "content",required = false) String content,
                     @RequestParam(value = "configComment",required = false) String configComment,
                     @RequestPart(value = "fileUpdates",required = false) MultipartFile[] fileUpdates,
                     @RequestParam(value = "updateName",required = false) String[] updateNames,
                     @RequestParam(value = "idsUpdateFile",required = false) Long[] idsUpdateFile,
                     @RequestParam(value = "idsUpdateName",required = false) Long[] idsUpdateName,
                     @RequestParam(value = "idDeletes",required = false) Long[] idDeletes,
                     @RequestPart(value = "fileAdds",required = false) MultipartFile[] fileAdds){

        if(fileAdds!=null){ fileService.addFileToLecture(id,fileAdds);}

        if(fileUpdates!=null || updateNames!=null){
            fileService.updateFileToLecutre(id,fileUpdates,idsUpdateFile,updateNames,idsUpdateName);
        }
        if(idDeletes!=null)
            fileService.deleteFileToLecture(idDeletes);

        fileService.updateLecture(id,content,configComment);
    }

    @GetMapping(value = "/course/lecture/file/download")
    public String  getVideoUrl (@RequestParam Long idLecture,@RequestParam String link){
        return fileService.getFileUrl(idLecture,link);
    }

    @GetMapping(value = "/course/lecture/file/list/download")
    public Map<String, String>  getListVideoUrl (@RequestParam Long id){
        return fileService.getListFileUrl(id);
    }

    @GetMapping(value = "/course/lecture/file/getstart")
    public ResponseEntity<FileEntity> getFileStart (@RequestParam Long idLecture){
        return ResponseEntity.ok().body(fileService.getFileStart(idLecture));
    }

    @PostMapping(value = "/course/lecture/file/test/delete")
    public void getFileStart (@RequestBody Long[] ids){
        fileService.deleteFileToLecture(ids);
    }
}
