export const typeText: Record<string, string> = { LOST: '寻物启事', FOUND: '招领启事' }
export const categoryText: Record<string, string> = { CARD: '证件卡片', DIGITAL: '数码设备', BOOK: '书籍资料', CLOTHING: '衣物配饰', KEY: '钥匙', SPORTS: '运动用品', OTHER: '其他物品' }
export const statusText: Record<string, string> = { PENDING_REVIEW: '待审核', SEARCHING: '寻找中', CLAIMING: '认领中', FOUND: '已找回', REJECTED: '已驳回', CLOSED: '已关闭', PENDING: '待处理', APPROVED: '已通过', COMPLETED: '已完成' }
export const categories = Object.entries(categoryText).map(([value, label]) => ({ value, label }))
export function formatTime(value: string) { return value ? value.replace('T', ' ').slice(0, 16) : '-' }
