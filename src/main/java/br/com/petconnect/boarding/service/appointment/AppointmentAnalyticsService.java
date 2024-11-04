package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsPercentageDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentAnalyticsService {
    private final AppointamentRepository appointamentRepository;
    public AppointmentSummaryResponseDto getTotalAppointmentsByUserAndServiceType() {
        List<Object[]> results = appointamentRepository.getTotalAppointmentsByUserAndServiceType();
        List<AppointmentSummaryDto> summaryList = new ArrayList<>();

        Long overallTotalAppointments = 0L;

        for (Object[] result : results) {
            Long userId = ((Number) result[0]).longValue();
            String userName = (String) result[1];
            String email = (String) result[2];
            String cpf = (String) result[3];
            Boolean isActive = (Boolean) result[4];
            String serviceType = (String) result[5];
            Long totalAppointments = ((Number) result[6]).longValue();
            overallTotalAppointments = ((Number) result[7]).longValue(); // Obtém o total geral uma vez

            summaryList.add(new AppointmentSummaryDto(
                    userId, userName, email, cpf, isActive, serviceType, totalAppointments
            ));
        }

        return new AppointmentSummaryResponseDto(summaryList,overallTotalAppointments);
    }

    public List<MonthlyAppointmentsPercentageDto> getAppointmentsCountAndPercentageByMonth() {
        List<Object[]> results = appointamentRepository.getAppointmentsCountAndPercentageByMonth();
        List<MonthlyAppointmentsPercentageDto> monthlyAppointments = new ArrayList<>();

        for (Object[] result : results) {
            String month = (String) result[0];
            Integer year = (Integer) result[1];
            String petType = (String) result[2];
            Long totalAppointments = ((Number) result[3]).longValue();
            Double percentage = ((Number) result[4]).doubleValue();

            monthlyAppointments.add(new MonthlyAppointmentsPercentageDto(month, year, petType, totalAppointments, percentage));
        }

        return monthlyAppointments;
    }

}
