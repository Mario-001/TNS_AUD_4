from dataclasses import dataclass
import sqlite3
from contextlib import closing


def delivery_fee(order_cents: int, express: bool) -> int:
    if not 1 <= order_cents <= 1_000_000:
        raise ValueError("Order value outside range")
    fee = 0 if order_cents >= 5_000 else 500
    return fee + (300 if express else 0)


@dataclass(frozen=True)
class Shipment:
    reference: str
    customer: str
    fee_cents: int
    label: str


class LabelGateway:
    def create_label(self, customer):
        raise NotImplementedError


class CarrierSdk:
    def account(self, account_id):
        raise NotImplementedError


class CarrierAccount:
    def labels(self):
        raise NotImplementedError


class LabelApi:
    def create(self, customer):
        raise NotImplementedError


class CarrierAdapter(LabelGateway):
    def __init__(self, sdk):
        self.sdk = sdk

    def create_label(self, customer):
        return self.sdk.account("teaching").labels().create(customer)


class ShipmentService:
    def __init__(self, repository, gateway):
        self.repository = repository
        self.gateway = gateway

    def book(self, reference, customer, order_cents, express):
        if not reference or not reference.strip():
            raise ValueError("Reference required")
        if not customer or not customer.strip():
            raise ValueError("Customer required")
        fee = delivery_fee(order_cents, express)
        label = self.gateway.create_label(customer)
        if not isinstance(label, str) or not label.strip():
            raise RuntimeError("Carrier returned empty label")
        shipment = Shipment(reference, customer, fee, label)
        self.repository.save(shipment)
        return shipment


class ShipmentRepository:
    def save(self, shipment):
        raise NotImplementedError

    def find(self, reference):
        raise NotImplementedError


class SqliteShipmentRepository(ShipmentRepository):
    def __init__(self, path):
        self.path = path

    def initialize(self):
        with closing(sqlite3.connect(self.path)) as connection, connection:
            connection.execute("CREATE TABLE shipments (reference TEXT PRIMARY KEY, customer TEXT NOT NULL, fee INTEGER NOT NULL, label TEXT NOT NULL)")

    def save(self, shipment):
        with closing(sqlite3.connect(self.path)) as connection, connection:
            connection.execute("INSERT INTO shipments VALUES (?, ?, ?, ?)",
                               (shipment.reference, shipment.customer, shipment.fee_cents, shipment.label))

    def find(self, reference):
        with closing(sqlite3.connect(self.path)) as connection, connection:
            row = connection.execute("SELECT reference, customer, fee, label FROM shipments WHERE reference=?", (reference,)).fetchone()
            return Shipment(*row) if row else None
