export interface ApiResponse<T> { success: boolean; message: string; data: T }
export interface User { id: number; username: string; nickname: string; phone: string; role: 'USER' | 'ADMIN' }
export interface LoginResult { token: string; user: User }
export interface Item {
  id: number; postType: 'LOST' | 'FOUND'; title: string; description: string
  category: string; location: string; occurredAt: string; contact: string; imageUrl?: string
  status: string; reviewComment?: string; publisherId: number; publisherName: string; createdAt: string
}
export interface Page<T> { content: T[]; totalElements: number; totalPages: number; number: number; size: number }
export interface Claim { id: number; itemId: number; itemTitle: string; applicantId: number; applicantName: string; proof: string; status: string; handleComment?: string; createdAt: string }
export interface Notice { id: number; title: string; content: string; read: boolean; createdAt: string }
