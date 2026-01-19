package hello.boassebackend.domain.partner.controller;

import hello.boassebackend.domain.partner.dto.PartnerCreateRequest;
import hello.boassebackend.domain.partner.dto.PartnerItem;
import hello.boassebackend.domain.partner.dto.PartnerListResponse;
import hello.boassebackend.domain.partner.dto.PartnerUpdateRequest;
import hello.boassebackend.domain.partner.service.PartnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/partners")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    @GetMapping
    public ResponseEntity<PartnerListResponse> getPartners() {
        PartnerListResponse response = partnerService.getPartners();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartnerItem> getPartner(@PathVariable Long id) {
        PartnerItem response = partnerService.getPartner(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PartnerItem> createPartner(
            @ModelAttribute PartnerCreateRequest request) throws IOException {
        Long partnerId = partnerService.createPartner(request);
        PartnerItem response = partnerService.getPartner(partnerId);
        return ResponseEntity.created(URI.create("/api/v1/partners/" + partnerId))
                .body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PartnerItem> updatePartner(
            @PathVariable Long id,
            @ModelAttribute PartnerUpdateRequest request) throws IOException {
        Long partnerId = partnerService.updatePartner(id, request);
        PartnerItem response = partnerService.getPartner(partnerId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePartner(@PathVariable Long id) {
        partnerService.deletePartner(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }
}
