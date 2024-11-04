package br.com.petconnect.boarding.service.appointment;

import br.com.petconnect.boarding.dto.AppointmentSummaryDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsGroupedDto;
import br.com.petconnect.boarding.dto.MonthlyAppointmentsPercentageDto;
import br.com.petconnect.boarding.dto.response.AppointmentSummaryResponseDto;
import br.com.petconnect.boarding.repositories.user.AppointamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public List<MonthlyAppointmentsGroupedDto> getAppointmentsCountAndPercentageByMonth() {
        List<Object[]> results = appointamentRepository.getAppointmentsCountAndPercentageByMonth();

        // Agrupando os dados por mês e ano
        Map<String, Map<Integer, List<MonthlyAppointmentsPercentageDto>>> groupedByMonthAndYear =
                results.stream().collect(Collectors.groupingBy(
                        result -> (String) result[0],  // Agrupa pelo mês
                        Collectors.groupingBy(
                                result -> (Integer) result[1],  // Agrupa pelo ano
                                Collectors.mapping(result -> {
                                    String petType = (String) result[2];
                                    Long totalAppointments = ((Number) result[3]).longValue();
                                    Double percentage = ((Number) result[4]).doubleValue();

                                    return new MonthlyAppointmentsPercentageDto(petType, totalAppointments, percentage);
                                }, Collectors.toList())
                        )
                ));

        // Criando a lista final agrupada
        List<MonthlyAppointmentsGroupedDto> groupedData = new ArrayList<>();

        for (Map.Entry<String, Map<Integer, List<MonthlyAppointmentsPercentageDto>>> monthEntry : groupedByMonthAndYear.entrySet()) {
            String month = monthEntry.getKey();

            for (Map.Entry<Integer, List<MonthlyAppointmentsPercentageDto>> yearEntry : monthEntry.getValue().entrySet()) {
                Integer year = yearEntry.getKey();
                List<MonthlyAppointmentsPercentageDto> appointments = yearEntry.getValue();

                // Calculando o total de agendamentos no mês
                Long totalAppointmentsInMonth = appointments.stream()
                        .mapToLong(MonthlyAppointmentsPercentageDto::getTotalAppointments)
                        .sum();

                groupedData.add(new MonthlyAppointmentsGroupedDto(month, year, totalAppointmentsInMonth, appointments));
            }
        }

        return groupedData;
    }

}
