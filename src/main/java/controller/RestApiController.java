package controller;

import dto.BoardCommentsDto;
import dto.NoticeDto;
import dto.SwitchSoundDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mapper.KeyboardMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import service.KeyboardService;
import java.util.Iterator;

import java.io.*;
import java.util.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@Slf4j
public class RestApiController {
    @Autowired
    private KeyboardService keyboardService;

    @Autowired
    private KeyboardMapper keyboardMapper;

    @GetMapping("/api/getSwitchSound/{ssUUID}")
    public void getSwitchSound(@PathVariable("ssUUID") String ssUUID, HttpServletResponse response) throws Exception {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String UPLOAD_PATH = "C:\\Temp\\";
        System.out.println(">>>>>>>>>>>>>>>>>>>>    " + ssUUID);
        System.out.println("++++++++++++++++++++++" + response);

        try {
            response.setHeader("Content-Disposition", "inline;");
            byte[] buf = new byte[1024];
            fis = new FileInputStream(UPLOAD_PATH + ssUUID + ".mp3");
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(response.getOutputStream());
            int read;
            while ((read = bis.read(buf, 0, 1024)) != -1) {
                bos.write(buf, 0, read);
            }
        } finally {
            bos.close();
            bis.close();
            fis.close();
        }
    }

    @GetMapping("/api/docker/{ssUUID}")
    public ResponseEntity<Map<String, Object>> dockerList(@PathVariable("ssUUID") String ssUUID)
            throws Exception {
        String ssUuid = ssUUID + ".mp3";
        final String command = "" // "docker container run -d --rm -w /my-app -v c:\\test:/my-app " <--여긴 도커 경로 넣어야함
                + ssUuid;

        Process process = null;
        Map<String, Object> result = new HashMap<>();
        List<String> uuids = new ArrayList<>();
        result.put("uuids", uuids);
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/IsDockerRun")
    public ResponseEntity<Boolean> isDockerRun() {
        final String command = "docker container ls";
        boolean isRunning = false;
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> list = reader.lines().toList();

            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String line = iterator.next();
                if (line.contains("deepin3809/spleeter")) { // "deepin3809/spleeter" 이 부분에 맞게 도커 뭐 이름이든 경로든 설정해야함
                    isRunning = true;
                    break;
                }
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(isRunning);
        return ResponseEntity.ok(isRunning);
    }

    @GetMapping("/api/savedSound/{ssUUID}")
    public List<String> savedSound(@PathVariable("ssUUID") String ssUUID) throws Exception {
        String path = "C:\\keyboard_test\\" + ssUUID + "\\";
        File file = new File(path);

        File[] files = file.listFiles();
        List<String> fileNames = new ArrayList<>();

        for (File f : files) {
            String fileName = f.getName();
            fileNames.add(fileName);
            System.out.println("=================" + fileNames);
        }
        return fileNames;
    }

    ;

    @GetMapping("/api/getSavedSound/{ssUUID}/{fn}")
    public void getSavedSound(@PathVariable("ssUUID") String ssUUID, HttpServletResponse response,
                              @PathVariable("fn") String fn) throws Exception {
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        String path = "C:\\keyboard_test\\" + ssUUID + "\\" + fn;
        System.out.println(">>>>>>>>>>>>>>>>>>>>    " + ssUUID);
        System.out.println("111111111111111" + fn);
        System.out.println("++++++++++++++++++++++" + response);
        try {
            response.setHeader("Content-Disposition", "inline;");
            byte[] buf = new byte[1024];
            fis = new FileInputStream(path);
            bis = new BufferedInputStream(fis);
            bos = new BufferedOutputStream(response.getOutputStream());
            int read;
            while ((read = bis.read(buf, 0, 1024)) != -1) {
                bos.write(buf, 0, read);
            }
        } finally {

        }
    }

    @GetMapping("/api/downloadSavedSound/{ssUUID}/{fileName:.+}")
    public void downloadSavedSound(@PathVariable("ssUUID") String ssUUID,
                                   @PathVariable("fileName") String fileName, HttpServletResponse response) throws Exception {
        String filePath = "C:\\keyboard_test\\" + ssUUID + "\\" + fileName;
        File file = new File(filePath);
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            try (FileInputStream inputStream = new FileInputStream(file);
                 ServletOutputStream outputStream = response.getOutputStream()) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
                outputStream.flush();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @PostMapping("/api/insertSoundForSaving/{bIdx}")
    public ResponseEntity<Map<String, Object>> insertSoundForSaving(@PathVariable("ssIdx")

                                                                    int bIdx, //여기 bIdx 맞는지 아닌지 확인해야함 bIdx가 아니라 ssIdx 일지도 모름 > 바꿈
                                                                    @RequestPart(value = "files", required = false)
                                                                    MultipartFile[] files) throws Exception {
        String UPLOAD_PATH = "C:\\keyboard_test\\";
        int insertedCount = 0;
        String uuid = UUID.randomUUID().toString();
        List<String> fileNames = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        try {
            for (MultipartFile mf : files) {
                String originFileName = mf.getOriginalFilename();
                try {
                    File f = new File(UPLOAD_PATH + File.separator + uuid + ".mp3");
                    System.out.println("---------------------------" + f);
                    mf.transferTo(f);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                fileNames.add(originFileName);
                int insertedConut = 0;
                insertedConut++;

                SwitchSoundDto switchSoundDto = new SwitchSoundDto();
                switchSoundDto.setSsTitle(originFileName);
                switchSoundDto.setSsUUID(uuid);
//                switchSoundDto.setSsIdx(ssIdx);
                keyboardService.insertSound(switchSoundDto);
            }

            if (insertedCount > 0) {
                result.put("uuid", uuid);
                result.put("fileNames", fileNames);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                result.put("message", "No files uploaded");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result.put("message", "파일 업로드 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 게시판 댓글
    @PostMapping("/api/insertBoardComments/{bIdx}")
    public ResponseEntity<Map<String, Object>> insertBoardComments(@RequestBody BoardCommentsDto boardCommentsDto,
                                                                   @PathVariable("bIdx") int bIdx) throws Exception {
        int insertedCount = 0;
        try {
            boardCommentsDto.setBcIdx(bIdx);
            boardCommentsDto.getUserNickname(); //-> 이 부분 getUserId 가 아니라 UserNickname 으로 바꿔야할지도 체크체크!!!

            insertedCount = keyboardService.insertBoardComments(boardCommentsDto);
            if (insertedCount > 0) {
                Map<String, Object> result = new HashMap<>();
                result.put("message", "정상적으로 등록되었습니다.");
                result.put("bcIdx", boardCommentsDto.getBcIdx());

                return ResponseEntity.status(HttpStatus.OK).body(result);
            } else {
                Map<String, Object> result = new HashMap<>();
                result.put("message", "등록된 내용이 없습니다.");
                result.put("count", insertedCount);
                return ResponseEntity.status(HttpStatus.OK).body(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> result = new HashMap<>();
            result.put("message", "등록 중 오류가 발생했습니다.");
            result.put("count", insertedCount);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    @GetMapping("/api/openBoardComments/{bIdx}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> openBoardComments(@PathVariable("bIdx") int bIdx) throws Exception{
        List<BoardCommentsDto> list = keyboardService.selectBoardCommentsList(bIdx);
        Map<String, Object> result = new HashMap<>();
        result.put("selectBoardCommentsList", list);
        if(result != null && result.size() > 0){
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/api/BoardComments/{bcIdx}")
    public ResponseEntity<Object> updateBoardComments(@PathVariable("bcIdx") int bcIdx, @RequestBody BoardCommentsDto boardCommentsDto)
            throws Exception {
        try {
            boardCommentsDto.setBcIdx(bcIdx);
            keyboardService.updateBoardComments(boardCommentsDto);
            return ResponseEntity.status(HttpStatus.OK).body(1);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    @DeleteMapping("/api/BoardCommentsDelete/{bcIdx}")
    public ResponseEntity<Object> deleteBoardComments(@PathVariable("bcIdx") int bcIdx) throws Exception {
        try {
            keyboardService.deleteBoardComments(bcIdx);
            return ResponseEntity.status(HttpStatus.OK).body(1);
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }

    //특정 공지 불러오기 - 여기 들어간 notice 부분 다 고쳐야 할 것 같음  +  announcementDto 하나 만들고
    @GetMapping("/api/noticeList")
    public ResponseEntity<List<NoticeDto>> NoticeList() throws Exception {
        List<NoticeDto> list =  keyboardService.noticeList();
        if(list == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(list);
        }
    }

    //특정 공지 불러오기 - 여기 들어간 notice 부분 다 고쳐야 할 것 같음 + announcementDto 하나 만들고
    @GetMapping("/api/noticeDetail/{nIdx}")
    public ResponseEntity<NoticeDto> noticeDetail(@PathVariable("nIdx") int nIdx) throws Exception{
        NoticeDto noticeDto = keyboardService.noticeDetail(nIdx);
        if(noticeDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(noticeDto);
        }
    }
}
