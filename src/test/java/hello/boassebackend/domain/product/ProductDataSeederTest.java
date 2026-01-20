package hello.boassebackend.domain.product;

import hello.boassebackend.domain.product.entity.Product;
import hello.boassebackend.domain.product.entity.Product.ProductCategory;
import hello.boassebackend.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProductDataSeederTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void seedProducts() {
        // 기존 데이터 삭제
        productRepository.deleteAll();
        System.out.println("Deleted all existing products.");

        List<Product> products = new ArrayList<>();

        // 1. SMART_MOBILITY (스마트 모빌리티)
        products.add(Product.builder()
                .title("Boasse-M1 자율주행 드론")
                .category(ProductCategory.SMART_MOBILITY)
                .image("https://images.unsplash.com/photo-1527977966376-1c8408f9f108?auto=format&fit=crop&w=800&q=80")
                .description("4K 카메라가 탑재된 고정밀 산업용 매핑 및 감시 드론입니다.")
                .detail("Boasse-M1은 산업 현장 점검 및 지형 매핑을 위해 설계되었습니다. 30분의 비행 시간, 장애물 회피 기능, 실시간 RTK 위치 제어 기능을 제공합니다.")
                .specs("{\"비행 시간\": \"30분\", \"운용 범위\": \"5km\", \"카메라\": \"4K 60fps\"}")
                .features("{\"GPS\": \"RTK 정밀 측위\", \"센서\": \"라이다 + 비전\", \"통신\": \"5G/LTE\"}")
                .isMainFeatured(true)
                .build());

        products.add(Product.builder()
                .title("Boasse-Scoot Pro 전동 킥보드")
                .category(ProductCategory.SMART_MOBILITY)
                .image("https://images.unsplash.com/photo-1595568579997-768a88523c04?auto=format&fit=crop&w=800&q=80")
                .description("도심형 공유 플랫폼을 위한 IoT 연동 전동 킥보드입니다.")
                .detail("견고한 알루미늄 프레임과 교체형 배터리 시스템을 갖추고 있으며, IoT 모듈이 내장되어 실시간 위치 추적 및 원격 잠금 제어가 가능합니다.")
                .specs("{\"최고 속도\": \"25km/h\", \"주행 거리\": \"40km\", \"무게\": \"15kg\"}")
                .features("{\"IoT\": \"4G/GPS\", \"브레이크\": \"듀얼 디스크\", \"타이어\": \"통고무 타이어\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("트래픽플로우 AI 센서")
                .category(ProductCategory.SMART_MOBILITY)
                .image("https://images.unsplash.com/photo-1567789884554-0b844b596c61?auto=format&fit=crop&w=800&q=80")
                .description("엣지 AI 처리를 이용한 스마트 신호 최적화 센서입니다.")
                .detail("실시간으로 차량 흐름과 보행자 밀집도를 감지하여 신호등 타이밍을 최적화하고, 교통 혼잡을 최대 30%까지 줄여줍니다.")
                .specs("{\"해상도\": \"1080p\", \"AI 칩\": \"NPU 5TOPS\", \"전원\": \"PoE\"}")
                .features("{\"감지 대상\": \"차량/보행자\", \"연동\": \"SCATS/SCOOT\", \"방수방진\": \"IP67\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("플릿가드 트래커")
                .category(ProductCategory.SMART_MOBILITY)
                .image("https://images.unsplash.com/photo-1580674684081-7617fbf3d745?auto=format&fit=crop&w=800&q=80")
                .description("물류 차량 관리를 위한 실시간 GPS 및 차량 진단 장치입니다.")
                .detail("차량의 상태, 운전자 주행 습관, 위치 정보를 실시간으로 모니터링합니다. OBD-II 포트에 직접 연결하여 간편하게 설치할 수 있습니다.")
                .specs("{\"인터페이스\": \"OBD-II\", \"통신\": \"LTE-M\", \"백업 배터리\": \"500mAh\"}")
                .features("{\"알림\": \"과속/공회전\", \"지오펜싱\": \"무제한\", \"보고서\": \"주간 리포트\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("EV-ChargeHub S 충전기")
                .category(ProductCategory.SMART_MOBILITY)
                .image("https://images.unsplash.com/photo-1620802051782-726fae1a1197?auto=format&fit=crop&w=800&q=80")
                .description("앱 연동 기능을 갖춘 스마트 전기차 충전 스테이션입니다.")
                .detail("상업용 및 가정용으로 적합한 고속 AC 충전기입니다. RFID 카드 인증 및 모바일 앱 결제를 지원합니다.")
                .specs("{\"출력\": \"11kW/22kW\", \"커넥터\": \"Type 2\", \"등급\": \"IP54\"}")
                .features("{\"앱\": \"iOS/Android\", \"부하 분산\": \"동적 제어\", \"프로토콜\": \"OCPP 1.6\"}")
                .isMainFeatured(true)
                .build());

        // 2. SMART_FACTORY (스마트 팩토리)
        products.add(Product.builder()
                .title("RoboArm X500 협동 로봇")
                .category(ProductCategory.SMART_FACTORY)
                .image("https://images.unsplash.com/photo-1563720223526-78f961db6e8a?auto=format&fit=crop&w=800&q=80")
                .description("정밀 조립 작업을 위한 협동 로봇 팔(Cobot)입니다.")
                .detail("사람과 함께 작업하기에 안전하며, 픽앤플레이스, 나사 체결, 본딩 등의 반복 작업을 0.05mm의 반복 정밀도로 자동화합니다.")
                .specs("{\"가반 하중\": \"5kg\", \"작업 반경\": \"850mm\", \"반복 정밀도\": \"+/- 0.05mm\"}")
                .features("{\"안전 규격\": \"ISO 10218-1\", \"프로그래밍\": \"드래그 앤 드롭\", \"비전\": \"옵션\"}")
                .isMainFeatured(true)
                .build());

        products.add(Product.builder()
                .title("팩토리아이 비전 시스템")
                .category(ProductCategory.SMART_FACTORY)
                .image("https://images.unsplash.com/photo-1504917595217-d4dc5ebe6122?auto=format&fit=crop&w=800&q=80")
                .description("자동 품질 검사를 위한 AI 기반 머신 비전 시스템입니다.")
                .detail("고속 생산 라인에서 불량품을 실시간으로 감지합니다. 적은 수의 샘플 이미지만으로도 딥러닝 모델 학습이 가능합니다.")
                .specs("{\"검사 속도\": \"1000 ppm\", \"카메라\": \"최대 4대\", \"인터페이스\": \"GigE Vision\"}")
                .features("{\"AI\": \"결함 탐지\", \"OCR\": \"라벨 판독\", \"대시보드\": \"웹 기반\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("컨베이어센스 IoT")
                .category(ProductCategory.SMART_FACTORY)
                .image("https://images.unsplash.com/photo-1581091226825-a6a2a5aee158?auto=format&fit=crop&w=800&q=80")
                .description("예지 보전을 위한 진동 및 온도 모니터링 센서입니다.")
                .detail("모터와 베어링에 부착하여 고장이 발생하기 전에 미리 예측합니다. 이상 징후 발생 시 유지보수 팀에게 자동으로 알림을 전송합니다.")
                .specs("{\"센서\": \"3축 가속도 + 온도\", \"배터리\": \"3년\", \"무선\": \"LoRaWAN\"}")
                .features("{\"분석\": \"클라우드 AI\", \"알림\": \"SMS/이메일\", \"설치\": \"자석 부착형\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("세이프티존 라이다")
                .category(ProductCategory.SMART_FACTORY)
                .image("https://images.unsplash.com/photo-1581093458791-9f3c3900df4b?auto=format&fit=crop&w=800&q=80")
                .description("위험 구역 방호를 위한 산업용 안전 레이저 스캐너입니다.")
                .detail("위험한 기계 주변에 2D 안전 커튼을 생성합니다. 사람이 경고 구역에 진입하면 즉시 장비 작동을 중단시킵니다.")
                .specs("{\"감지 범위\": \"안전 5m / 경고 20m\", \"각도\": \"270도\", \"반응 속도\": \"60ms\"}")
                .features("{\"인증\": \"SIL 2 / PL d\", \"구역\": \"설정 가능\", \"인터페이스\": \"EtherCAT\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("LogiBot AGV 운송 로봇")
                .category(ProductCategory.SMART_FACTORY)
                .image("https://images.unsplash.com/photo-1586528116311-ad8dd3c8310d?auto=format&fit=crop&w=800&q=80")
                .description("효율적인 창고 운송을 위한 무인 운반 차량(AGV)입니다.")
                .detail("팔레트와 박스를 운반하는 자율 주행 로봇입니다. 마그네틱 테이프 없이 SLAM 네비게이션을 사용하여 이동합니다.")
                .specs("{\"적재 하중\": \"500kg\", \"속도\": \"1.5m/s\", \"리프트\": \"100mm\"}")
                .features("{\"네비게이션\": \"LiDAR SLAM\", \"배터리\": \"자동 충전\", \"안전\": \"360도 범퍼\"}")
                .isMainFeatured(true)
                .build());

        // 3. SMART_FARM (스마트 팜)
        products.add(Product.builder()
                .title("AgriDrone 방제 드론")
                .category(ProductCategory.SMART_FARM)
                .image("https://images.unsplash.com/photo-1508614589041-895b88991e3e?auto=format&fit=crop&w=800&q=80")
                .description("농약 및 비료 살포를 위한 정밀 농업용 드론입니다.")
                .detail("넓은 밭을 빠르고 균일하게 작업할 수 있습니다. 정밀 살포 기술을 통해 약제 사용량을 최대 40%까지 절감합니다.")
                .specs("{\"탱크 용량\": \"20L\", \"살포 폭\": \"6m\", \"작업 효율\": \"시간당 10헥타르\"}")
                .features("{\"레이더\": \"지형 추적\", \"노즐\": \"원심 분사\", \"접이식\": \"가능\"}")
                .isMainFeatured(true)
                .build());

        products.add(Product.builder()
                .title("소일센스 Pro")
                .category(ProductCategory.SMART_FARM)
                .image("https://images.unsplash.com/photo-1464226184884-fa280b87c399?auto=format&fit=crop&w=800&q=80")
                .description("무선 토양 수분, 온도 및 영양분 감지 센서입니다.")
                .detail("토양에 매립하여 실시간으로 데이터를 전송합니다. 관수 및 비료 공급 일정을 최적화하는 데 도움을 줍니다.")
                .specs("{\"측정 깊이\": \"30cm\", \"항목\": \"수분, 온도, EC\", \"통신\": \"Zigbee\"}")
                .features("{\"전원\": \"태양광\", \"데이터\": \"히스토리 그래프\", \"앱\": \"FarmOS 연동\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("그린하우스 클라이밋 마스터")
                .category(ProductCategory.SMART_FARM)
                .image("https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=800&q=80")
                .description("온실 환경 자동 제어 시스템입니다.")
                .detail("센서 데이터를 기반으로 환기창, 팬, 히터, 차광막을 자동으로 제어하여 최적의 생육 환경을 유지합니다.")
                .specs("{\"제어 구역\": \"최대 8개\", \"센서\": \"온습도/CO2/조도\", \"입력\": \"24VAC\"}")
                .features("{\"로직\": \"PID 제어\", \"원격\": \"클라우드 접속\", \"알람\": \"서리/고온 경보\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("아쿠아모니터 IoT")
                .category(ProductCategory.SMART_FARM)
                .image("https://images.unsplash.com/photo-1583212235753-526a92e9e4d?auto=format&fit=crop&w=800&q=80")
                .description("양식업 및 어류 양식을 위한 수질 모니터링 시스템입니다.")
                .detail("pH, 용존 산소량(DO), 수온을 지속적으로 모니터링하여 어류의 건강을 지킵니다.")
                .specs("{\"프로브\": \"pH, DO, 수온, 암모니아\", \"주기\": \"1분\", \"세척\": \"자동 와이퍼\"}")
                .features("{\"알림\": \"산소 부족 경보\", \"그래프\": \"실시간 확인\", \"내구성\": \"해수 사용 가능\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("크롭캠 생육 트래커")
                .category(ProductCategory.SMART_FARM)
                .image("https://images.unsplash.com/photo-1625246333195-f8196ba083aa?auto=format&fit=crop&w=800&q=80")
                .description("식물 건강과 성장 속도를 모니터링하는 AI 카메라 시스템입니다.")
                .detail("타임랩스 및 분광 분석을 통해 질병을 조기에 발견하고 생체량(Biomass) 증가를 측정합니다.")
                .specs("{\"해상도\": \"4K\", \"스펙트럼\": \"RGB + NDVI\", \"마운트\": \"폴대/레일\"}")
                .features("{\"AI\": \"질병 반점 탐지\", \"클라우드\": \"저장소 제공\", \"보고서\": \"성장률 분석\"}")
                .isMainFeatured(false)
                .build());

        // 4. SMART_BUILDING (스마트 빌딩)
        products.add(Product.builder()
                .title("스마트액세스 게이트")
                .category(ProductCategory.SMART_BUILDING)
                .image("https://images.unsplash.com/photo-1541888946425-d81bb19240f5?auto=format&fit=crop&w=800&q=80")
                .description("안전한 빌딩 출입을 위한 안면 인식 스피드 게이트입니다.")
                .detail("체온 측정 기능이 포함된 비접촉 생체 인식 시스템입니다. 오피스 로비에서 빠르고 안전한 출입을 보장합니다.")
                .specs("{\"인식 속도\": \"0.3초/인\", \"용량\": \"50,000명\", \"타입\": \"스윙 게이트\"}")
                .features("{\"위조 방지\": \"듀얼 IR\", \"로그\": \"근태 관리 연동\", \"연결\": \"Wiegand\"}")
                .isMainFeatured(true)
                .build());

        products.add(Product.builder()
                .title("에코라이트 컨트롤러")
                .category(ProductCategory.SMART_BUILDING)
                .image("https://images.unsplash.com/photo-1550751827-4bd3774c3f58b?auto=format&fit=crop&w=800&q=80")
                .description("재실 감지 센서가 내장된 에너지 절약형 자동 조명 시스템입니다.")
                .detail("사람과 없는 구역의 조명을 끄거나 어둡게 조절합니다. 자연광 수준에 따라 밝기를 자동으로 최적화합니다.")
                .specs("{\"프로토콜\": \"DALI-2\", \"센서\": \"PIR + 조도\", \"절감률\": \"최대 60%\"}")
                .features("{\"스케줄\": \"앱 제어\", \"하베스팅\": \"자연광 연동\", \"그룹\": \"존별 제어\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("에어퓨어 HVAC 매니저")
                .category(ProductCategory.SMART_BUILDING)
                .image("https://images.unsplash.com/photo-1615818451847-c0cc5b1cc8d1?auto=format&fit=crop&w=800&q=80")
                .description("대형 건물을 위한 스마트 공기질 및 온도 제어 시스템입니다.")
                .detail("CO2와 VOC(휘발성 유기 화합물) 농도를 감지하여 신선한 공기 유입량을 조절합니다. 쾌적하고 생산적인 환경을 조성합니다.")
                .specs("{\"입력\": \"CO2, PM2.5, VOC\", \"출력\": \"0-10V / Modbus\", \"디스플레이\": \"터치스크린\"}")
                .features("{\"건강\": \"바이러스 케어 모드\", \"에너지\": \"에코 모드\", \"트렌드\": \"월간 데이터\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("엘리베이터플로우 AI")
                .category(ProductCategory.SMART_BUILDING)
                .image("https://images.unsplash.com/photo-1542914042-e30030563467?auto=format&fit=crop&w=800&q=80")
                .description("승객 목적지 기반의 최적 엘리베이터 배차 시스템입니다.")
                .detail("출퇴근 시간대 혼잡을 줄이기 위해 같은 층으로 가는 승객을 그룹화하여 탑승시킵니다.")
                .specs("{\"알고리즘\": \"목적층 제어\", \"연동\": \"키오스크/앱\", \"효과\": \"대기시간 20% 단축\"}")
                .features("{\"VIP\": \"우선 탑승 모드\", \"출입\": \"사원증 연동\", \"통계\": \"트래픽 분석\"}")
                .isMainFeatured(false)
                .build());

        products.add(Product.builder()
                .title("세이프가드 화재 IoT")
                .category(ProductCategory.SMART_BUILDING)
                .image("https://images.unsplash.com/photo-1558002638-1091a1661116?auto=format&fit=crop&w=800&q=80")
                .description("원격 알림 및 자가 진단 기능을 갖춘 스마트 화재 감지기입니다.")
                .detail("화재 발생 시 관리자의 스마트폰으로 즉시 알림을 전송합니다. 수동 점검 없이도 매주 자동으로 센서 상태를 점검합니다.")
                .specs("{\"센서\": \"광전식\", \"배터리\": \"10년 수명\", \"무선\": \"메쉬 네트워크\"}")
                .features("{\"자가 진단\": \"자동 주간 점검\", \"알림\": \"모바일 푸시\", \"위치\": \"실 단위 파악\"}")
                .isMainFeatured(false)
                .build());

        productRepository.saveAll(products);
        System.out.println("Seeded " + products.size() + " products successfully.");
    }
}