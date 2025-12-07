package com.poc.es.elasticsearchspringboot.controller;
import com.poc.es.elasticsearchspringboot.exception.RecordNotFoundException;
import com.poc.es.elasticsearchspringboot.model.Dump;
import com.poc.es.elasticsearchspringboot.model.DumpOperation;
import com.poc.es.elasticsearchspringboot.model.DumpSearchRequest;
import com.poc.es.elasticsearchspringboot.service.DumpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;

@RestController
public class DumpController {
    @Autowired
    private DumpService dumpService;

    // @Autowired
    // private FileStorageService fileStorageService;

    

    @GetMapping("/dumps")
    public ResponseEntity<List<Dump>> fetchDumps() throws RecordNotFoundException, IOException {
        // System.out.print("Fetching all dumps");
        List<Dump> dump = dumpService.fetchDumps();
        return ResponseEntity.ok(dump);
    }

    @PostMapping("/dumps/fetchWithMust")
    public ResponseEntity<List<Dump>> fetchDumpsWithMustQuery(@RequestBody Dump dumpSearchRequest)
            throws IOException {
        List<Dump> dumps = dumpService.fetchDumpsWithMustQuery(dumpSearchRequest);
        return ResponseEntity.ok(dumps);
    }
    @PostMapping("/dumps/search")
    public ResponseEntity<List<Dump>> searchDumps(@RequestBody DumpSearchRequest dumpSearchRequest)
            throws IOException {
        List<Dump> dumps = dumpService.searchDumps(dumpSearchRequest);
        return ResponseEntity.ok(dumps);
    }

    @PostMapping("/dumps/fetchWithShould")
    public ResponseEntity<List<Dump>> fetchDumpsWithShouldQuery(@RequestBody Dump dumpSearchRequest)
            throws IOException {
        List<Dump> dumps = dumpService.fetchDumpsWithShouldQuery(dumpSearchRequest);
        return ResponseEntity.ok(dumps);
    }
    @PostMapping("/dumps/fetchWithScroll")
    public ResponseEntity<List<Dump>> fetchDumpsWithScrollQuery(@RequestBody Dump dumpSearchRequest, @RequestParam(required = false) String scrollId)
            throws IOException {
        List<Dump> dumps = dumpService.fetchDumpsWithScrollQuery(dumpSearchRequest, scrollId);
        return ResponseEntity.ok(dumps);
    }

    @PostMapping("/dumps")
    public ResponseEntity<Dump> insertRecords(@RequestBody Dump dump) throws IOException {
        System.out.println(dump);
        String status = dumpService.insertDump(dump);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Dump> entity = new ResponseEntity<>(dump, headers, HttpStatus.CREATED);
        return entity;
    }

    @PostMapping("/dumps/bulk")
    public ResponseEntity<List<Dump>> bulkInsertDumps(@RequestBody List<Dump> dumps) throws IOException {
        boolean isSuccess = dumpService.bulkInsertDumps(dumps);
        if (isSuccess) {
            return ResponseEntity.ok(dumps);
        } else {
            return ResponseEntity.internalServerError().body(null);
        }
    }
    @GetMapping("/dumps/{id}")
    public ResponseEntity<Dump> fetchDumpById(@PathVariable("id") String id)
            throws RecordNotFoundException, IOException {
        Dump dump = dumpService.fetchDumpById(id);
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<Dump> entity = new ResponseEntity<>(dump, headers, HttpStatus.CREATED);
        return entity;
    }

    // @PostMapping("/dumps/bulkRequest")
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

    //     JsonData data = dumpService.parseJSONFile(stream);

    //     System.out.println("Parsed Data" + data);

    //     // return new UploadFileResponse(fileName, fileDownloadUri,
    //     //         file.getContentType(), file.getSize());
    //     return ResponseEntity.ok("Done!");
    // }

    @DeleteMapping("/dumps/{id}")
    public ResponseEntity<String> deleteDump(@PathVariable("id") String id) throws IOException {
        String status = dumpService.deleteDumpById(id);
        return ResponseEntity.noContent().build();
    }
    // @DeleteMapping("/dumps")
    // public ResponseEntity<String> deleteAllDumps(@RequestBody List<String> idList) throws IOException {
    //     boolean status = dumpService.deleteDumps(idList);
    //     return ResponseEntity.noContent().build();
    // }

    @PutMapping("/dumps")
    public ResponseEntity<Dump> updateDump(@RequestBody DumpOperation dumpOp) throws IOException {
        String status = dumpService.updateDump(dumpOp);
        return ResponseEntity.ok(dumpOp.getDumpData());
    }
}
