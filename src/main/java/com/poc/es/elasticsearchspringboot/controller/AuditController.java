package com.poc.es.elasticsearchspringboot.controller;

import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Audit;
import com.poc.es.elasticsearchspringboot.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@RestController
public class AuditController {
    @Autowired
    private AuditService auditService;

    // @Autowired
    // private FileStorageService fileStorageService;

    @GetMapping("/audits/{id}")
    public ResponseEntity<Audit> fetchAuditById(@PathVariable("id") String id)
            throws RecordNotFoundException, IOException {
        Audit audit = auditService.fetchAuditById(id);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Audit> entity = new ResponseEntity<>(audit, headers, HttpStatus.CREATED);
        return entity;
    }

    @GetMapping("/audits")
    public ResponseEntity<List<Audit>> fetchAudits() throws RecordNotFoundException, IOException {
        List<Audit> audit = auditService.fetchAudits();
        return ResponseEntity.ok(audit);
    }

    @PostMapping("/audits/fetchWithMust")
    public ResponseEntity<List<Audit>> fetchAuditsWithMustQuery(@RequestBody Audit auditSearchRequest)
            throws IOException {
        List<Audit> audits = auditService.fetchAuditsWithMustQuery(auditSearchRequest);
        return ResponseEntity.ok(audits);
    }

    @PostMapping("/audits/fetchWithShould")
    public ResponseEntity<List<Audit>> fetchAuditsWithShouldQuery(@RequestBody Audit auditSearchRequest)
            throws IOException {
        List<Audit> audits = auditService.fetchAuditsWithShouldQuery(auditSearchRequest);
        return ResponseEntity.ok(audits);
    }

    @PostMapping("/audits")
    public ResponseEntity<Audit> insertRecords(@RequestBody Audit audit) throws IOException {
        System.out.println(audit);
        String status = auditService.insertAudit(audit);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Audit> entity = new ResponseEntity<>(audit, headers, HttpStatus.CREATED);
        return entity;
    }

    @PostMapping("/audits/bulk")
    public ResponseEntity<List<Audit>> bulkInsertAudits(@RequestBody List<Audit> audits) throws IOException {
        boolean isSuccess = auditService.bulkInsertAudits(audits);
        if (isSuccess) {
            return ResponseEntity.ok(audits);
        } else {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    // @PostMapping("/audits/bulkRequest")
    // public ResponseEntity<String> bulkReq(@RequestParam("file") MultipartFile file) throws JSONException, IOException {
    //     // System.out.println(input);
    //     // ObjectMapper mapper = new ObjectMapper();
    //     // JsonNode node = mapper.readTree(input);
    //     // System.out.println(node);
    //     // JSONObject jsonObject= new JSONObject(input);
    //     // System.out.println(jsonObject);
    //     String fileName = fileStorageService.storeFile(file);

    //     // String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
    //     //         .path("/downloadFile/")
    //     //         .path(fileName)
    //     //         .toUriString();
    //     InputStream stream = new BufferedInputStream(file.getInputStream());
    //     // String result = IOUtils.toString(stream, StandardCharsets.UTF_8);

    //     JsonData data = auditService.parseJSONFile(stream);

    //     System.out.println("Parsed Data" + data);

    //     // return new UploadFileResponse(fileName, fileDownloadUri,
    //     //         file.getContentType(), file.getSize());
    //     return ResponseEntity.ok("Done!");
    // }

    @DeleteMapping("/audits/{id}")
    public ResponseEntity<String> deleteAudit(@PathVariable("id") String id) throws IOException {
        String status = auditService.deleteAuditById(id);
        return ResponseEntity.noContent().build();
    }
    // @DeleteMapping("/audits")
    // public ResponseEntity<String> deleteAllAudits(@RequestBody List<String> idList) throws IOException {
    //     boolean status = auditService.deleteAudits(idList);
    //     return ResponseEntity.noContent().build();
    // }

    @PutMapping("/audits")
    public ResponseEntity<Audit> updateAudit(@RequestBody Audit audit) throws IOException {
        String status = auditService.updateAudit(audit);
        return ResponseEntity.ok(audit);
    }
}
