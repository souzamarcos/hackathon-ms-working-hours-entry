package com.fiap.burger.entity.order;

import com.fiap.burger.entity.notification.NotificationType;

public enum OrderStatus {
    CANCELADO {
        @Override
        public NotificationType asNotificationType() {
            return NotificationType.PAGAMENTO_NAO_CONFIRMADO;
        }
    },
    AGUARDANDO_PAGAMENTO,
    RECEBIDO {
        @Override
        public NotificationType asNotificationType() {
            return NotificationType.PAGAMENTO_CONFIRMADO;
        }
    },
    EM_PREPARACAO,
    PRONTO {
        @Override
        public NotificationType asNotificationType() {
            return NotificationType.PEDIDO_PRONTO;
        }
    },
    FINALIZADO;

    public NotificationType asNotificationType() {
        return null;
    }
}
