package com.hust.soict.ict.aims.mapper;

import com.hust.soict.ict.aims.models.dto.response.payments.PaymentStatusResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.QRCodeResponse;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.QRCodePaymentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PaymentMapper {
    QRCodeResponse toQRCodeResponse(QRCode qrCode);
    PaymentStatusResponse toPaymentStatusResponse(QRCodePaymentStatus paymentStatus);
}
