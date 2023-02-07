CREATE OR REPLACE FUNCTION update_store_inventory()
    RETURNS trigger
    LANGUAGE plpgsql
AS $$
DECLARE order_store_id INT;
BEGIN
    SELECT origin_store_id
    INTO order_store_id
    FROM "order" AS o
    WHERE o.id = NEW.order_id;

    IF NOT NEW.is_service THEN
        UPDATE inventory
        SET on_hand = on_hand - 1
        WHERE store_id = order_store_id
            AND product_id = NEW.product_id;
    END IF;
RETURN NULL;
END; $$;