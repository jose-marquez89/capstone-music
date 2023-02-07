CREATE OR REPLACE TRIGGER inventory_update
AFTER INSERT ON order_line
FOR EACH ROW
EXECUTE PROCEDURE update_store_inventory();