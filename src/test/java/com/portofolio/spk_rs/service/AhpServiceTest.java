package com.portofolio.spk_rs.service;

import com.portofolio.spk_rs.dto.AhpRequest;
import com.portofolio.spk_rs.model.Kriteria;
import com.portofolio.spk_rs.repository.KriteriaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AhpServiceTest {

    @Mock
    private KriteriaRepository kriteriaRepository;

    @InjectMocks
    private AhpService ahpService;

    @Test
    void hitungDanSimpanBobot_SkenarioLimaKriteriaSamaPenting() {
        // ==========================================
        // A (ARRANGE) - Siapkan 5 Kriteria Asli SPK RSUD
        // ==========================================
        Kriteria c1 = new Kriteria(); c1.setId(1L); c1.setKode("C1"); c1.setNama("Kualitas Kerja");
        Kriteria c2 = new Kriteria(); c2.setId(2L); c2.setKode("C2"); c2.setNama("Keterampilan Teknis");
        Kriteria c3 = new Kriteria(); c3.setId(3L); c3.setKode("C3"); c3.setNama("Kedisiplinan / Absensi");
        Kriteria c4 = new Kriteria(); c4.setId(4L); c4.setKode("C4"); c4.setNama("Kerjasama Tim");
        Kriteria c5 = new Kriteria(); c5.setId(5L); c5.setKode("C5"); c5.setNama("Tanggung Jawab");

        List<Kriteria> dummyKriteria = Arrays.asList(c1, c2, c3, c4, c5);

        // Program otak Database Palsu
        when(kriteriaRepository.findAll()).thenReturn(dummyKriteria);
        when(kriteriaRepository.saveAll(anyList())).thenReturn(dummyKriteria);

        // Request perbandingan kosong (agar semua dinilai sama rata = 1.0)
        AhpRequest request = new AhpRequest();
        request.setPerbandingan(new HashMap<>());

        // ==========================================
        // A (ACT) - Jalankan Mesin Utama AHP
        // ==========================================
        List<Kriteria> hasil = ahpService.hitungDanSimpanBobot(request);

        // ==========================================
        // A (ASSERT) - Buktikan Kebenaran Logika Matematikanya
        // ==========================================
        assertNotNull(hasil, "Hasil tidak boleh null");
        assertEquals(5, hasil.size(), "Jumlah kriteria harus tepat 5");

        // Karena 5 kriteria dihitung sama rata, maka bobot masing-masing harus 1/5 = 0.20
        assertEquals(0.20, hasil.get(0).getBobotAHP(), 0.01, "Bobot C1 (Kualitas Kerja) meleset!");
        assertEquals(0.20, hasil.get(1).getBobotAHP(), 0.01, "Bobot C2 (Keterampilan) meleset!");
        assertEquals(0.20, hasil.get(2).getBobotAHP(), 0.01, "Bobot C3 (Kedisiplinan) meleset!");
        assertEquals(0.20, hasil.get(3).getBobotAHP(), 0.01, "Bobot C4 (Kerjasama) meleset!");
        assertEquals(0.20, hasil.get(4).getBobotAHP(), 0.01, "Bobot C5 (Tanggung Jawab) meleset!");

        // Buktikan fungsi simpan ke database berjalan 1x
        verify(kriteriaRepository, times(1)).saveAll(dummyKriteria);
    }

    @Test
    void hitungDanSimpanBobot_SkenarioAdaPrioritasHRD() {
        // ==========================================
        // A (ARRANGE) - Siapkan 5 Kriteria Asli
        // ==========================================
        Kriteria c1 = new Kriteria(); c1.setId(1L); c1.setKode("C1"); c1.setNama("Kualitas Kerja");
        Kriteria c2 = new Kriteria(); c2.setId(2L); c2.setKode("C2"); c2.setNama("Keterampilan Teknis");
        Kriteria c3 = new Kriteria(); c3.setId(3L); c3.setKode("C3"); c3.setNama("Kedisiplinan / Absensi");
        Kriteria c4 = new Kriteria(); c4.setId(4L); c4.setKode("C4"); c4.setNama("Kerjasama Tim");
        Kriteria c5 = new Kriteria(); c5.setId(5L); c5.setKode("C5"); c5.setNama("Tanggung Jawab");

        List<Kriteria> dummyKriteria = Arrays.asList(c1, c2, c3, c4, c5);

        when(kriteriaRepository.findAll()).thenReturn(dummyKriteria);
        when(kriteriaRepository.saveAll(anyList())).thenReturn(dummyKriteria);

        // ==========================================
        // SIMULASI INPUT WEBSITE DARI HRD
        // ==========================================
        Map<Long, Map<Long, Double>> perbandingan = new HashMap<>();

        // HRD mensetting: C1 (ID 1) Sangat Kuat / 5x lebih penting dibanding C4 (ID 4)
        Map<Long, Double> lawanC1 = new HashMap<>();
        lawanC1.put(4L, 5.0); // Perhatikan tambahan huruf 'L' di angka 4

        // HRD mensetting: C3 (ID 3) Sedikit lebih penting / 3x dibanding C4 (ID 4)
        Map<Long, Double> lawanC3 = new HashMap<>();
        lawanC3.put(4L, 3.0);

        // Masukkan ke matriks utama
        perbandingan.put(1L, lawanC1);
        perbandingan.put(3L, lawanC3);

        AhpRequest request = new AhpRequest();
        request.setPerbandingan(perbandingan);

        // ==========================================
        // A (ACT) - Jalankan Mesin
        // ==========================================
        List<Kriteria> hasil = ahpService.hitungDanSimpanBobot(request);

        // ==========================================
        // A (ASSERT) - Buktikan Hukum AHP
        // ==========================================

        // 1. Hukum Mutlak AHP: Total semua bobot harus mendekati 1.0 (100%)
        double totalBobot = 0;
        for (Kriteria k : hasil) {
            totalBobot += k.getBobotAHP();
        }
        assertEquals(1.0, totalBobot, 0.01, "Fatal! Total bobot AHP tidak sama dengan 1.0");

        // 2. Hukum Logika Prioritas: Karena C1 dinilai 5x lebih penting dari C4,
        // maka bobot C1 (Index 0) harus dipastikan lebih besar dari C4 (Index 3).
        assertTrue(hasil.get(0).getBobotAHP() > hasil.get(3).getBobotAHP(),
                "Logika salah: C1 seharusnya memiliki bobot lebih besar dari C4!");

        // 3. Pastikan C3 (Index 2) juga lebih besar dari C4 (Index 3)
        assertTrue(hasil.get(2).getBobotAHP() > hasil.get(3).getBobotAHP(),
                "Logika salah: C3 seharusnya memiliki bobot lebih besar dari C4!");
    }
}