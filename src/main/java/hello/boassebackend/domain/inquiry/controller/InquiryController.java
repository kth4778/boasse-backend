package hello.boassebackend.domain.inquiry.controller;

import hello.boassebackend.domain.inquiry.dto.InquiryCreateRequest;
import hello.boassebackend.domain.inquiry.dto.InquiryItem;
import hello.boassebackend.domain.inquiry.dto.InquiryListResponse;
import hello.boassebackend.domain.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inquiries")
public class InquiryController {

    private final InquiryService inquiryService;

    @PostMapping
    public ResponseEntity<Void> createInquiry(@RequestBody InquiryCreateRequest request) {
        inquiryService.createInquiry(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<InquiryListResponse> getInquiries(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        InquiryListResponse response = inquiryService.getInquiries(page, limit);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InquiryItem> getInquiry(@PathVariable Long id) {
        InquiryItem response = inquiryService.getInquiry(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInquiry(@PathVariable Long id) {
        inquiryService.deleteInquiry(id);
        return ResponseEntity.ok().build();
    }
}
