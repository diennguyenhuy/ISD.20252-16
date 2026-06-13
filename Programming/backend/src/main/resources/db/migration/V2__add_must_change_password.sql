-- Add must_change_password flag to user table.
-- Set by admin during password reset; forces the user to change their password on next login.
ALTER TABLE "user" ADD COLUMN must_change_password BOOLEAN NOT NULL DEFAULT FALSE;
